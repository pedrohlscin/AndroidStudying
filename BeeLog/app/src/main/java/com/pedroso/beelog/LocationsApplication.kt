package com.pedroso.beelog

import android.app.Application
import com.pedroso.beelog.database.repository.LocationRepository
import com.pedroso.beelog.database.room.BeeLogRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LocationsApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { BeeLogRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { LocationRepository(database.locationDao()) }
}