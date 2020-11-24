package com.pedroso.beelog.database.repository

import androidx.annotation.WorkerThread
import com.pedroso.beelog.database.dao.LocationDao
import com.pedroso.beelog.database.data.Location
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: LocationDao) {

    val allLocations: Flow<List<Location>> = locationDao.loadAllLocations()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(location: Location){
        locationDao.insert(location)
    }
}