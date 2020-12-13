package com.pedroso.beelog.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pedroso.beelog.database.converters.Converters
import com.pedroso.beelog.database.data.Location
import com.pedroso.beelog.database.dao.LocationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Location::class), version = 2, exportSchema = false)
@TypeConverters(Converters::class)
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
                    // Delete all content here.
                    database.locationDao().deleteAll()
                }
            }
        }
    }
}