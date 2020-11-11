package com.pedroso.beelog

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Math.acos
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
                val p1 = Coord(38.8901100,-77.0319900)
                val p2 = Coord(38.890245, -77.03199)
                val distance = distance(p1, p2)
                location.text = distance.toString()
            }
            fusedLocationClient.lastLocation.addOnFailureListener {
                Toast.makeText(applicationContext, "Error trying to get last location", Toast.LENGTH_SHORT)
            }
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "Location Permission Needed!", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_FINE_LOCATION)
        }
    }

    fun distance(p1 : Coord, p2 : Coord) : Double{
        val  R = 6372.795477598
        return R * acos(sin(p1.lat) * sin(p2.lat) + cos(p1.lat) * cos(p2.lat) * cos(p1.lon - p2.lon)) * 1000
    }

    data class Coord(val lat : Double, val lon : Double){ }
}