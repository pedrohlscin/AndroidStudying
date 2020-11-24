package com.pedroso.beelog

import android.Manifest
import android.Manifest.*
import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.location.Location
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices

class RegisterLocationWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams){
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
//    override fun doWork(): Result { }
//        if(checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED){
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient((applicationContext))
//            var local : String? = null
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> null
//            }
//    }
}