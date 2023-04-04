package com.example.testsensor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class MainViewModel : ViewModel(), DataClient.OnDataChangedListener {

    private val _hr = MutableLiveData<Int>()
    val hr : LiveData<Int>
        get() = _hr
    private val _changed = MutableLiveData<Boolean>(false)
    val changed : LiveData<Boolean>
    get() = _changed

    init {
        hr.observeForever {
            value ->
            _changed.value=true
        }
    }
    override fun onDataChanged(dataEvents: DataEventBuffer) {
       Log.d("datachanged","datachanged")
        viewModelScope.launch {
            dataEvents.forEach { event ->
                if (event.type == DataEvent.TYPE_CHANGED) {
                    // DataItem Changed
                    val item = event.dataItem
                    if (item.uri.path?.compareTo("/heart_rate") == 0) {
                        val dataMap = DataMapItem.fromDataItem(item).dataMap
                        _hr.value=dataMap.getInt("heart_rate_key")
                        Log.d("dataischanged",dataMap.getInt("heart_rate_key").toString())
                    }
                }
            }
        }
    }

}