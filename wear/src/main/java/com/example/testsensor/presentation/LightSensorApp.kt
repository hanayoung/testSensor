package com.example.testsensor.presentation

import android.content.Context
import android.hardware.SensorManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.example.testsensor.presentation.data.HealthServicesRepository
import com.example.testsensor.presentation.theme.TestSensorTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.wearable.DataClient

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LightSensorApp(
    sensorManager: SensorManager,
    dataClient: DataClient,
    context:Context
) {
    TestSensorTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            timeText = { TimeText() }
        ) {

                val viewModel: LightSensorViewModel = viewModel(
                factory = lightSensorViewModelFactory(
                   sensorManager=sensorManager
                )
            )
                val enabled by viewModel.enabled.collectAsState()
                LightSensorScreen(
                    context=context,
                    sensorManager = sensorManager,
                    enabled = enabled,
                    onButtonClick = { viewModel.toggleEnabled() }
                )


        }
    }
}
