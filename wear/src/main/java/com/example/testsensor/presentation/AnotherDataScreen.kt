package com.example.testsensor.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.services.client.data.DataTypeAvailability
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.testsensor.R
import com.example.testsensor.presentation.theme.TestSensorTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AnotherDataScreen(
    vo2: Double,
    availability: DataTypeAvailability,
    enabled: Boolean,
    onButtonClick: () -> Unit,
    permissionState: PermissionState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HrLabel(
            hr = vo2,
            availability = availability
        )
        Button(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = {
                if (permissionState.status.isGranted) {
                    Log.d("permission","is granted")
                    onButtonClick()
                } else {
                    Log.d("permission","is not granted")
                    permissionState.launchPermissionRequest()
                }
            }
        ) {
            val buttonTextId = if (enabled) {
                R.string.stop
            } else {
                R.string.start
            }
            Text(stringResource(buttonTextId))
        }
    }
}

@ExperimentalPermissionsApi
@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showBackground = false,
    showSystemUi = true
)
@Composable
fun AnotherDataScreenPreview() {
    val permissionState = object : PermissionState {
        override val permission = "android.permission.ACTIVITY_RECOGNITION"
        override val status: PermissionStatus = PermissionStatus.Granted
        override fun launchPermissionRequest() {}
    }
    TestSensorTheme {
        MeasureDataScreen(
            hr = 65.0,
            availability = DataTypeAvailability.AVAILABLE,
            enabled = false,
            onButtonClick = {},
            permissionState = permissionState
        )
    }
}
