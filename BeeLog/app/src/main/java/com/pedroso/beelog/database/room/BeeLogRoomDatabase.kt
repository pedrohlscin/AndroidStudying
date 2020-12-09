package com.pedroso.beelog.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pedroso.beelog.database.data.Location
import com.pedroso.beelog.database.dao.LocationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Location::class), version = 1, exportSchema = false)
public abstract class BeeLogRoomDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object{
        @Volatile
        private var INSTANCE: BeeLogRoomDatabase? = null

        fun getDatabase(context: Context, scope : CoroutineScope): BeeLogRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, BeeLogRoomDatabase::class.java,
                    "beelog_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(LocationDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class LocationDatabaseCallback( private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var locationDao = database.locationDao()

                    // Delete all content here.
                    locationDao.deleteAll()

                    // Add sample words.
                    var location = Location(1.21, 1.22)
                    locationDao.insert(location)
                    var location2 = Location(1.23, 1.24)
                    locationDao.insert(location2)
                }
            }
        }
    }
}