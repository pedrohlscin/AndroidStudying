package com.pedroso.beelog.database.dao

import androidx.room.*
import com.pedroso.beelog.database.data.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(loc: Location)

    @Query("SELECT * FROM location_table")
    fun loadAllLocations(): Flow<List<Location>>

    @Query("Delete from location_table")
    suspend fun deleteAll()
}