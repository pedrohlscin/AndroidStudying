package com.pedroso.beelog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pedroso.beelog.database.data.Location
import com.pedroso.beelog.viewmodel.LocationViewModel
import com.pedroso.beelog.viewmodel.LocationViewModelFactory
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val newLocationActivityRequestCode = 1
    private val locationViewModel : LocationViewModel by viewModels{
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
            locations.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewLocationActivity::class.java)
            startActivityForResult(intent, newLocationActivityRequestCode)
        }


        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationService()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newLocationActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewLocationActivity.EXTRA_REPLY)?.let { reply ->
                var lat : String = reply.split(';')[0]
                var lon : String = reply.split(';')[1]
                val location = Location(Random.nextLong(), lat as Double,  lon as Double)
                locationViewModel.insert(location)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10 && grantResults.size == 1) {
            startLocationService()
        } else {
            Toast.makeText(this, "Para o app funcionar corretamente é preciso compartilhar sua localização", Toast.LENGTH_SHORT).show()
        }

    }

    fun startLocationService() {
        //inicializar service por aqui
        //antes verificar se ele já não foi inicializado
        val locationWorkRequest = PeriodicWorkRequestBuilder<GetLocation>(5, TimeUnit.SECONDS).build()
        WorkManager.getInstance(applicationContext).enqueue(locationWorkRequest)
    }

}
