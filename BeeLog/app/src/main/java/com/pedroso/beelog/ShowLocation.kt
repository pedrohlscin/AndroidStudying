package com.pedroso.beelog

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Math.acos
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ShowLocation : AppCompatActivity() {
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient((applicationContext))
            var local : String? = null
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                local = "longitude: " + location?.longitude + " - Latitude: " + location?.latitude
                val location = findViewById<TextView>(R.id.location)

                val p1 = Location("point A")
                p1.latitude = -8.0395008
                p1.longitude = -34.9072609
                val p2 = Location("point B")
                p2.latitude = -8.0393597
                p2.longitude = -34.9072954
                val distance = p1.distanceTo(p2)
                location.text = distance.toString()
            }
            fusedLocationClient.lastLocation.addOnFailureListener {
                Toast.makeText(applicationContext, "Error trying to get last location", Toast.LENGTH_SHORT)
            }
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "Location Permission Needed!", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_FINE_LOCATION)
        }
    }
}