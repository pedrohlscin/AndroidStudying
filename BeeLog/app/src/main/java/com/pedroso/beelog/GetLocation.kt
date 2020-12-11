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
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.pedroso.beelog.database.room.BeeLogRoomDatabase
import com.pedroso.beelog.database.room.BeeLogRoomDatabase_Impl
import com.pedroso.beelog.viewmodel.LocationViewModel
import com.pedroso.beelog.viewmodel.LocationViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class GetLocation(appContext: Context, workerParameters: WorkerParameters) : Worker(appContext, workerParameters) {

    var fusedLocationClient: FusedLocationProviderClient? = null

    private val repository = (applicationContext as LocationsApplication).repository


    override fun doWork(): Result {
        getAndSaveLocation()
        return Result.success()
    }

    private fun getAndSaveLocation() {

        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(fusedLocationClient == null) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
            }
            val apply = LocationRequest.create().apply {
                fastestInterval=5000
                priority=LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val builder : LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
                .addLocationRequest(apply)
            fusedLocationClient!!.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("LOCATION", "aeee ${location}")
                GlobalScope.launch {
                    repository.insert(
                        com.pedroso.beelog.database.data.Location(
                            latitude = location!!.latitude,
                            longitude = location!!.longitude
                        )
                    )
                }
            }
        }
    }
}