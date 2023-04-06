package com.example.testsensor.presentation

import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testsensor.presentation.data.HealthServicesRepository
import com.example.testsensor.presentation.data.MeasureMessage
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException

class HrViewModel(
    private val sensorManager: SensorManager
) : ViewModel() {
    val enabled: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val hr: MutableState<Double> = mutableStateOf(0.0)
    val availability: MutableState<DataTypeAvailability> =
        mutableStateOf(DataTypeAvailability.UNKNOWN)

    fun updateHrData(dataClient: DataClient, hr: Double){
        viewModelScope.launch(Dispatchers.IO) {
            val putDataMapRequest = PutDataMapRequest.create("/heart_rate")
            putDataMapRequest.dataMap.putInt("heart_rate_key", hr.toInt())
            val putDataReq = putDataMapRequest.asPutDataRequest()
            putDataReq.setUrgent()
            val putDataTask = dataClient.putDataItem(putDataReq)
            try {
                Tasks.await(putDataTask).apply {
                    Log.d("Updatehr in apply",hr.toString())
                }
            } catch (e: ExecutionException) {
                Log.d("UpdateLight", "updateCalories: Failure ${e.printStackTrace()}")
            } catch (e: InterruptedException) {
                Log.d("UpdateLight", "updateCalories: Failure ${e.printStackTrace()}")
            }
        }
    }
}

class HrViewModelFactory(
    private val sensorManager: SensorManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HrViewModel::class.java)) {
            return HrViewModel(sensorManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
