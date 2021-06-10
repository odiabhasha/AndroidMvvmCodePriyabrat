package com.app.codeby.priyabrat.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.app.codeby.priyabrat.Constants
import com.app.codeby.priyabrat.R
import com.app.codeby.priyabrat.data.Resource
import com.app.codeby.priyabrat.ui.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val TAG = "main_activity"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissions()
        val textView = findViewById<TextView>(R.id.text_view)
        val buttonRefresh = findViewById<Button>(R.id.button_refresh)
        buttonRefresh.setOnClickListener { checkPermissions() }
        mainViewModel.liveDataWeatherResponse.observe(this, {
            when(it) {
                Resource.Loading -> {
                    Log.d(TAG, "Response Loading")
                    textView.text = "Loading..."
                }
                is Resource.Success -> {
                    Log.d(TAG, "Success Response \n ${it.data}")
                    textView.text = it.data.toString()
                }
                is Resource.Error -> {
                    Log.d(TAG, "Error Response")
                    textView.text = "Error Loading Due to \n ${it.error}"
                }
            }
        })
    }

    /**
     * Checks for the required permissions, if not allowed this will request using requestPermissions
     * If permissions already given, this will call MainViewModel.getWeatherResponse
     */
    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                location?.let {
                    mainViewModel.latitude = it.latitude
                    mainViewModel.longitude = it.longitude
                }
                mainViewModel.getWeatherResponse()
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_LONG).show()
            }
            // Need to ask for the permissions
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), Constants.REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mainViewModel.getWeatherResponse()
            } else {
                Toast.makeText(this, "You have denied the Permission", Toast.LENGTH_LONG).show()
            }
        }
    }
}