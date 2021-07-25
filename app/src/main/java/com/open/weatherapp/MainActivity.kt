package com.open.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.open.weatherapp.databinding.ActivityMainBinding
import com.open.weatherapp.utils.Constants
import com.open.weatherapp.utils.WeatherPeriodicSync
import com.open.weatherapp.vm.WeatherVM
import com.tcs.mobile.androidpermission.PermissionChecker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var wifiState = false
    private var mPeriodicWorkRequest: PeriodicWorkRequest? = null
    var permissionlist = ArrayList<String>()
    private var isRequiredPermissionsGranted: Boolean = false
    private lateinit var permissionChecker: PermissionChecker
    private val viewModel: WeatherVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val wifi = this!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        wifiState = wifi!!.isWifiEnabled
        setSupportActionBar(binding.toolbar)

        initPerms()
        getLatLong()
//        periodicSync()
    }

    private fun periodicSync() {
        // We can Cancel Periodic work by ID or TAG too

        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        mPeriodicWorkRequest = PeriodicWorkRequest.Builder(
            WeatherPeriodicSync::class.java,
            2, TimeUnit.HOURS
        )
            .addTag("WeatherPeriodicSync")
            .setConstraints(networkConstraint)
            .build()

        WorkManager.getInstance(this).enqueue(mPeriodicWorkRequest!!)
    }

    private fun getLatLong() {

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // update after every 2 Hrs with weather data
        val locationRequest = LocationRequest().setInterval(7200000).setFastestInterval(7200000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        Constants.Latitude = location.latitude.toString()
                        Constants.Longitude = location.longitude.toString()
                        if (wifiState) {
                            viewModel.getWeatherReport(
                                Constants.Latitude,
                                Constants.Longitude,
                                Constants.APP_ID
                            )
                        }
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }


    private fun initPerms() {
        permissionlist.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionlist.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        permissionChecker = PermissionChecker.PermissionBuilder().with(this).listener(object :
            PermissionChecker.OnPermissionListener {
            override fun onAllRequiredPermissionsGranted(permissions: List<String?>) {
                Snackbar.make(
                    binding.constraintLayoutM,
                    resources.getString(R.string.permissions_all),
                    Snackbar.LENGTH_SHORT
                ).show()

                isRequiredPermissionsGranted = true
                if (isRequiredPermissionsGranted) {
                    getLatLong()
                }
            }

            override fun onPermissionsGranted(permissions: List<String?>) {
                Snackbar.make(
                    binding.constraintLayoutM,
                    resources.getString(R.string.permissions_granted),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onPermissionsDenied(permissions: List<String?>) {
                Snackbar.make(
                    binding.constraintLayoutM,
                    resources.getString(R.string.permissions_denied),
                    Snackbar.LENGTH_SHORT
                ).show()
                permissionChecker.requestAllPermissions(permissionlist.toTypedArray())
            }

            override fun neverAskPermissions(permissions: List<String?>) {
                if (permissions.isNotEmpty()) {
                    Snackbar.make(
                        binding.constraintLayoutM,
                        resources.getString(R.string.permission_required_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }).build()
        if (!checkIfPermissionGiven(permissionlist.toTypedArray())) {
            permissionChecker.requestAllPermissions(permissionlist.toTypedArray())
        } else {
            isRequiredPermissionsGranted = true
            getLatLong()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun checkIfPermissionGiven(permissionlist: Array<String>): Boolean {
        var allGranted = false
        for (grantResult in permissionlist) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    grantResult
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                allGranted = true
            } else {
                allGranted = false
                break
            }
        }
        return allGranted
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val wifi = this!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        wifiState = wifi!!.isWifiEnabled

        viewModel.getWeatherDB().observe(this, { weather ->
            if (weather != null) {
                binding.apply {
                    tvCityName.text = weather.city
                    tvDescription.text = "Description : " + weather.description
                    tvTemperature.text = "Temperature in Kelvin : " + weather.temperature
                    tvWind.text = "Wind Speed : " + weather.speed + "km/h"
                    tvCode.text = "Country Code : " + weather.country
                }
            }} )

        if (Constants.Latitude.isNotEmpty() && Constants.Longitude.isNotEmpty() && wifiState) {
            viewModel.getWeatherReport(Constants.Latitude, Constants.Longitude, Constants.APP_ID)
        } else {
            getLatLong()
        }

//        val handler = Handler()
//        handler.postDelayed({
//            try {
//                viewModel.getWeatherReport(Constants.Latitude, Constants.Longitude, Constants.APP_ID)
//            } catch (exception: java.lang.Exception) {
//            }
//        }, 7200000)
    }
}