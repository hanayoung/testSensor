/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.testsensor.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.testsensor.R
import com.example.testsensor.presentation.theme.TestSensorTheme
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutionException
import kotlin.coroutines.cancellation.CancellationException
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold

class MainActivity : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val nodeClient by lazy { Wearable.getNodeClient(this) }

    private val viewModel : MeasureDataViewModel by viewModels()
    private lateinit var lightViewModel : LightSensorViewModel
    private lateinit var hrViewModel : HrViewModel

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val healthServicesRepository = (application as MainApplication).healthServicesRepository

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightViewModel = ViewModelProvider(this,LightSensorViewModelFactory(this)).get(LightSensorViewModel::class.java)
        hrViewModel = ViewModelProvider(this,HrViewModelFactory(this)).get(HrViewModel::class.java)
        setContent {
//            AnotherDataApp(healthServicesRepository = healthServicesRepository, dataClient = dataClient)

            HrScreen(context = this)
            LightSensorApp(sensorManager,dataClient,this)

//            LightSensorScreen(context = this)

//            MeasureDataApp(healthServicesRepository = healthServicesRepository, dataClient = dataClient)

            hrViewModel.updateHrData(dataClient,hrViewModel.hr.value)
            lifecycleScope.launch {
                try {
                    val nodes = nodeClient.connectedNodes.await()
                    Log.d(TAG,"start nodes!")
                    nodes.map { node ->
                        async {
                            Log.d("nodeId",node.id)
                            messageClient.sendMessage(node.id, START_ACTIVITY_PATH, byteArrayOf())
                                .await()
                        }
                    }.awaitAll()
                    if(nodes.size != 0) {
                        Log.d(TAG, "Starting activity requests sent successfully")
                    }
                } catch (cancellationException: CancellationException) {
                    Log.d("sensorManager","cancel")
                    throw cancellationException
                } catch (exception: Exception) {
                    Log.d(TAG, "Starting activity failed: $exception")

                }
            }
        }
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()

    }
    companion object {
        private const val TAG = "MainActivity"
        private const val START_ACTIVITY_PATH = "/start-activity"
    }
}
