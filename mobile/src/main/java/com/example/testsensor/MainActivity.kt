package com.example.testsensor

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels()
    private val dataClient by lazy { Wearable.getDataClient(this)}
    var isRunning = false
    var changed=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.hrValue)
        val startBtn = findViewById<Button>(R.id.startBtn)
        val lineChart = findViewById<LineChart>(R.id.linechart)
        val addBtn = findViewById<Button>(R.id.addBtn)

        var input = Array<Double>(5, { Math.random() })

        viewModel.hr.observe(this, Observer {
            text.text = viewModel.hr.value.toString()
        })

        if (lineChart.data!=null||text.text.toString()!="Hello World!") {
            Log.d("changedValue",changed.toString())
            val temp = viewModel.hr.value!!.toDouble()
            input.plus(temp)
            Log.d("helloworld!",temp.toString())
            lifecycleScope.launch {
                val data = lineChart.data
                val dataSet = data.getDataSetByIndex(0)
                for (i in input.indices) {
                    if (i < dataSet.entryCount) {
                        dataSet.getEntryForIndex(i).y = input[i].toFloat()
                    } else {
                        delay(100)
                        dataSet.addEntry(Entry(i.toFloat(), input[i].toFloat()))
                        data.notifyDataChanged()
                    }
                    lineChart.notifyDataSetChanged()
                    lineChart.setVisibleXRangeMaximum(4f)
                    lineChart.moveViewToX(dataSet.entryCount.toFloat())
                    lineChart.invalidate()
                }
            }
        }
            addBtn.setOnClickListener {
                val temp = Array<Double>(5, { Math.random() })
                input += temp
                lifecycleScope.launch {
                    val data = lineChart.data
                    val dataSet = data.getDataSetByIndex(0)
                    for (i in input.indices) {
                        if (i < dataSet.entryCount) {
                            dataSet.getEntryForIndex(i).y = input[i].toFloat()
                        } else {
                            delay(100)
                            dataSet.addEntry(Entry(i.toFloat(), input[i].toFloat()))
                            data.notifyDataChanged()
                        }
                        lineChart.notifyDataSetChanged()
                        lineChart.setVisibleXRangeMaximum(4f)
                        lineChart.moveViewToX(dataSet.entryCount.toFloat())
                        lineChart.invalidate()
                    }
                }
            }

            startBtn.setOnClickListener {
                Log.d("isRunning",isRunning.toString())
                if (!isRunning) {
                    Log.d("start","start")
                    lifecycle.coroutineScope.launch {
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

                        for (i in input.indices) {
                            delay(100)
                            Log.d("moveViewtoX", "here")
                            data.addEntry(Entry(i.toFloat(), input[i].toFloat()), 0)
                            data.notifyDataChanged()

                            lineChart.apply {
                                notifyDataSetChanged()
                                Log.d("moveViewtoX", "after")
                                moveViewToX(data.entryCount.toFloat())
                                setVisibleXRangeMaximum(4f)
                                invalidate()
                            }
                        }
                    }
                    isRunning = false
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