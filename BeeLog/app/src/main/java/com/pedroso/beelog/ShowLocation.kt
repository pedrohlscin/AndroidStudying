package com.pedroso.beelog

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest

class ShowLocation : AppCompatActivity() {
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient((this))
        var local : String? = null
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                local = "altitude: " + location?.altitude + " - Latitude: " + location?.latitude
            }
        }else{
            val toast = Toast.makeText(applicationContext, "No permission", Toast.LENGTH_LONG)
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}