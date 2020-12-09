package com.pedroso.beelog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pedroso.beelog.database.room.BeeLogRoomDatabase
import com.pedroso.beelog.database.room.BeeLogRoomDatabase_Impl
import com.pedroso.beelog.viewmodel.LocationViewModel
import com.pedroso.beelog.viewmodel.LocationViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GetLocation(appContext: Context, workerParameters: WorkerParameters) : Worker(appContext, workerParameters) {

//    private val Loca

    private val repository = (applicationContext as LocationsApplication).repository

//    {
//        LocationViewModelFactory((application as LocationsApplication).repository)
//    }

    override fun doWork(): Result {
        getAndSaveLocation()
        return Result.success()
    }

    private fun getAndSaveLocation() {
        lateinit var fusedLocationClient: FusedLocationProviderClient

        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient((applicationContext))
            var local: String? = null
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("LOCATION", "aeee ${location}")
                GlobalScope.launch {
                    repository.insert(com.pedroso.beelog.database.data.Location(latitude = location!!.latitude, longitude =  location!!.longitude))
                }
            }
        }
    }
}