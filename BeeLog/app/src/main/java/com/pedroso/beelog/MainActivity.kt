package com.pedroso.beelog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pedroso.beelog.database.data.Location
import com.pedroso.beelog.viewmodel.LocationViewModel
import com.pedroso.beelog.viewmodel.LocationViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {

    var locations = mutableListOf<Location>()

    private val newLocationActivityRequestCode = 1
    var locationRequest: LocationRequest? = null

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((application as LocationsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = LocationListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        locationViewModel.allLocations.observe(this) { locations ->
            locations.let {
                adapter.submitList(it)
            }
        }

        val hasAccessCoarseLocation = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessFineLocation = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasAccessCoarseLocation && hasAccessFineLocation) {
//            startLocationService()
            startLocationUpdates()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10)
        }

    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
            fastestInterval = 500
        }

        val locationSettings = LocationSettingsRequest.Builder().apply {
            locationRequest?.let {
                addLocationRequest(it)
            }
        }.build()

        val settingsClient = LocationServices.getSettingsClient(this).apply {
            checkLocationSettings(locationSettings)
        }

        FusedLocationProviderClient(this).requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            Looper.myLooper()
        )
    }

    private fun onLocationChanged(lastLocation: android.location.Location?) {
        Log.d("LOCATION", "aeee ${lastLocation}")

        locations.add(
            Location(lastLocation!!.latitude, lastLocation!!.longitude, Calendar.getInstance().time)
        )
        checkIfItsAValidLocation()

    }

    private fun checkIfItsAValidLocation() {
        var itemsToRemove = mutableListOf<Location>()
        val listIterator = locations.listIterator()
        for(loc in locations){
            var locTemp = getLocation(loc)
            val nextLocation = listIterator.next()
            var nextLoc = getLocation(nextLocation)
            if(locTemp.distanceTo(nextLoc) < 30 && ((loc.date.time - nextLocation.date.time) / 1000 / 60) <= 15 ){
                itemsToRemove.add(loc)
                if(itemsToRemove.size >= 15){
                    locationViewModel.insert(loc)
                    locations.removeAll(itemsToRemove)
                    itemsToRemove.clear()
                }
            }else{
                locations.removeAll(itemsToRemove)
                itemsToRemove.clear()
            }
        }
    }

    private fun getLocation(loc: Location): android.location.Location {
        val temp = android.location.Location("")
        temp.latitude = loc.latitude
        temp.longitude = loc.longitude
        return temp
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 10 && grantResults.size == 1) {
//            startLocationService()
//        } else {
//            Toast.makeText(
//                this,
//                "Para o app funcionar corretamente é preciso compartilhar sua localização",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//    }
}