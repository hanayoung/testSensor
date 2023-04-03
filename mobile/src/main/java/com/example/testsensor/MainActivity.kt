package com.example.testsensor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels()
    private val dataClient by lazy { Wearable.getDataClient(this)}

    val localeToday = LocalDate.now(ZoneId.of("Asia/Manila"))
    val dawn = LocalTime.MIDNIGHT
    var permission = false

    val PERMISSIONS =
        setOf(
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getWritePermission(HeartRateRecord::class)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionsLauncher = requestPermissionsActivityContract()
        Log.d("permissionsLauncher",permissionsLauncher.toString())

        val text = findViewById<TextView>(R.id.hrValue)
        val per = findViewById<Button>(R.id.per)
        if(HealthConnectClient.isProviderAvailable(applicationContext)){
            val healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
            Log.d("healthconnect","client")
//            lifecycleScope.launch{
//                val granted = healthConnectClient.permissionController.getGrantedPermissions(PERMISSIONS)
//                if (granted.containsAll(PERMISSIONS)) {
//
//                }
//            }
            per.setOnClickListener {
                lifecycleScope.launch {
                    if(hasAllPermissions(permissions = PERMISSIONS,healthConnectClient)){
                        permission=true
                        Log.d("permission",permission.toString())
                    } }
            }

           if(permission){
               viewModel.hr.observe(this, Observer {
                   text.text=viewModel.hr.value.toString()
                   lifecycleScope.launch(Dispatchers.IO){
                       writeData(healthConnectClient = healthConnectClient)
                       Log.d("write success","write checking")
                   }
               })
               val btn = findViewById<Button>(R.id.button)
               btn.setOnClickListener {
                   lifecycleScope.launch(Dispatchers.IO) {
                       readHeartRatesByTimeRangeDay(healthConnectClient, LocalDateTime.of(localeToday, dawn), LocalDateTime.now())
                   }
               }
           }

        }

    }
    override fun onResume() {
        super.onResume()
        dataClient.addListener(viewModel)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(viewModel)
    }
}