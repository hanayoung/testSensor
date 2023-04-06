package com.example.testsensor

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels()
    private val dataClient by lazy { Wearable.getDataClient(this)}
    private lateinit var hrInput : Array<Double>
    private lateinit var lightInput : Array<Double>

    private val TAG = "mobileerror"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.hrValue)
        val lineChart = findViewById<LineChart>(R.id.linechart)

        val lightChart = findViewById<LineChart>(R.id.lightchart)
        val lightTxt = findViewById<TextView>(R.id.lightValue)

        viewModel.hr.observe(this, Observer {
            text.text = viewModel.hr.value.toString()
            addEntry(lineChart,"hr")

        })

        viewModel.light.observe(this,Observer{
            lightTxt.text = viewModel.light.value.toString()
            addEntry(lightChart,"light")
        })
        }

    private fun addEntry(lineChart:LineChart,type:String){
        var temp:Double = when(type){
            "hr" -> {
                viewModel.hr.value!!.toDouble()
            }
            "light" ->{
                viewModel.light.value!!.toDouble()
            }
            else ->{
                0.0
            }
        }
        if(::hrInput.isInitialized&&type=="hr"){ // lateinit한 input값이 초기화되어있는지 확인
            hrInput=hrInput.plus(temp) // input이 초기화되어있으니 요소를 추가
        }else if(::lightInput.isInitialized&&type=="light"){
            lightInput=lightInput.plus(temp)
        }else{
            val entries = ArrayList<Entry>()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, 0F))
            // 그래프 구현을 위한 LineDataSet 생성
            val dataset = LineDataSet(entries, "input")
            // 그래프 data 생성 -> 최종 입력 데이터
            val data = LineData(dataset)
            // chart.xml에 배치된 lineChart에 데이터 연결
            lineChart.data = data

            // 그래프 생성
            lineChart.animateXY(1, 1)
            when(type){
                "hr" ->{
                    hrInput=Array<Double>(1,{temp})
                }
                "light" -> {
                    lightInput=Array<Double>(1,{temp})
                }
            } // input 초기화 작업
        }

        lifecycleScope.launch {
            val data = lineChart.data // 연결된 데이터 가져옴
            val dataSet = data.getDataSetByIndex(0) // 0번째 위치의 데이터셋 가져옴
            if(type=="hr"){
                for (i in hrInput.indices) {
                    if (i < dataSet.entryCount) { // 기존 dataset에 있는 값은 추가할 필요가 없으므로 분기처리
                        dataSet.getEntryForIndex(i).y = hrInput[i].toFloat() // dataset에서 값 가져옴
                    } else {
                        delay(100)
                        dataSet.addEntry(Entry(i.toFloat(), hrInput[i].toFloat()))
                        data.notifyDataChanged()
                    }
                    lineChart.notifyDataSetChanged()
                    lineChart.setVisibleXRangeMaximum(4f)
                    lineChart.moveViewToX(dataSet.entryCount.toFloat())
                    lineChart.invalidate()
                }
            }else if(type=="light"){
                for (i in lightInput.indices) {
                    if (i < dataSet.entryCount) { // 기존 dataset에 있는 값은 추가할 필요가 없으므로 분기처리
                        dataSet.getEntryForIndex(i).y = lightInput[i].toFloat() // dataset에서 값 가져옴
                    } else {
                        delay(100)
                        dataSet.addEntry(Entry(i.toFloat(), lightInput[i].toFloat()))
                        data.notifyDataChanged()
                    }
                    lineChart.notifyDataSetChanged()
                    lineChart.setVisibleXRangeMaximum(4f)
                    lineChart.moveViewToX(dataSet.entryCount.toFloat())
                    lineChart.invalidate()
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