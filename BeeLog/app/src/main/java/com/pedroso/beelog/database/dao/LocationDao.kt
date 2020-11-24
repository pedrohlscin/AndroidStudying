package com.pedroso.beelog.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedroso.beelog.database.data.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(loc: Location)

    @Query("SELECT * FROM location_table")
    fun loadAllLocations(): Flow<List<Location>>
}