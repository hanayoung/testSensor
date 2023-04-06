package com.example.testsensor.presentation

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.getSystemService
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
fun AnotherDataApp(
    healthServicesRepository: HealthServicesRepository,
    dataClient: DataClient
) {
    TestSensorTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            timeText = { TimeText() }
        ) {
            val viewModel: MeasureDataViewModel = viewModel(
                factory = MeasureDataViewModelFactory(
                    healthServicesRepository = healthServicesRepository
                )
            )
            val enabled by viewModel.enabled.collectAsState()
            val vo2 by viewModel.vo2
            val availability by viewModel.vo2Availability
            val uiState by viewModel.vo2UiState

            if (uiState == UiState.Supported) {
                val permissionState = rememberPermissionState(
                    permission = PERMISSION,
                    onPermissionResult = { granted ->
                        if (granted) viewModel.toggleEnabled()
                    }
                )
                AnotherDataScreen(
                    vo2 = vo2,
                    availability = availability,
                    enabled = enabled,
                    onButtonClick = { viewModel.toggleEnabled() },
                    permissionState = permissionState
                )
                viewModel.updateHeartRate(dataClient=dataClient,hr=viewModel.hr.value)
            } else if (uiState == UiState.NotSupported) {
                NotSupportedScreen()
            }
        }
    }
}
