package com.pedroso.beelog.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedroso.beelog.database.data.Location
import com.pedroso.beelog.database.dao.LocationDao

@Database(entities = arrayOf(Location::class), version = 1, exportSchema = false)
public abstract class BeeLogRoomDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object{
        @Volatile
        private var INSTANCE: BeeLogRoomDatabase? = null
        fun getDatabase(context: Context): BeeLogRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, BeeLogRoomDatabase::class.java,
                    "beelog_database"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}