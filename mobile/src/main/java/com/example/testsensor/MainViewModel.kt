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

    private val _light = MutableLiveData<Int>()
    val light : LiveData<Int>
    get() = _light

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        viewModelScope.launch {
            dataEvents.forEach { event ->
                if (event.type == DataEvent.TYPE_CHANGED) {
                    // DataItem Changed
                    val item = event.dataItem
                    Log.d("itemchanged",item.toString())
                    if (item.uri.path?.compareTo("/heart_rate") == 0) {
                        val dataMap = DataMapItem.fromDataItem(item).dataMap
                        _hr.value=dataMap.getInt("heart_rate_key")
                        Log.d("dataischanged",dataMap.getInt("heart_rate_key").toString())
                    }
                    else if(item.uri.path?.compareTo("/light")==0){
                        val dataMap = DataMapItem.fromDataItem(item).dataMap
                        _light.value=dataMap.getInt("light_key")
                        Log.d("dataischanged",dataMap.getInt("light_key").toString())
                    }
                }
            }
        }
    }

}