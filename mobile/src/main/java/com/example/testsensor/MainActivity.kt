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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.hrValue)
        val startBtn = findViewById<Button>(R.id.startBtn)
        val lineChart = findViewById<LineChart>(R.id.linechart)
        val addBtn = findViewById<Button>(R.id.addBtn)

        var input = Array<Double>(5,{Math.random()})
        Log.d("first input",input[1].toString())

//        val coroutineClass = CoroutineClass(lineChart = lineChart,input=input)

        viewModel.hr.observe(this, Observer {
            text.text=viewModel.hr.value.toString()
        })

        addBtn.setOnClickListener {
            val temp = Array<Double>(5,{Math.random()})
            input+=temp
            lifecycleScope.launch{
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
//
//
//                val visibleRange = 3f // 보여줄 x축 범위
//                if (dataSet.entryCount > visibleRange) {
//                    Log.d("entryCount",dataSet.entryCount.toString())
//
//                } else {
//                    lineChart.setVisibleXRangeMaximum(visibleRange)
//                    lineChart.moveViewToX(0f)
//                }
//            val temp = Array<Double>(5,{Math.random()})
//            Log.d("changed input",temp[1].toString())
////            addEntry(input.size,temp.toInt(),lineChart)
//            input+=temp
//            lifecycleScope.launch{
//                val data = lineChart.data
//                val dataSet = data.getDataSetByIndex(0)
//
//                for (i in input.indices) {
//                    if (i < dataSet.entryCount) {
//                        dataSet.getEntryForIndex(i).y = input[i].toFloat()
//                    } else {
//                        dataSet.addEntry(Entry(i.toFloat(), input[i].toFloat()))
//                    }
//                }
//
//                data.notifyDataChanged()
//                lineChart.notifyDataSetChanged()
//                val visibleRange = 3f // 보여줄 x축 범위
//                if (dataSet.entryCount > visibleRange) {
//                    lineChart.setVisibleXRangeMaximum(visibleRange)
//                    lineChart.moveViewToX(dataSet.entryCount - 5f)
//                } else {
//                    lineChart.setVisibleXRangeMaximum(visibleRange)
//                    lineChart.moveViewToX(0f)
//                }
////                lineChart.moveViewToX(data.entryCount.toFloat() - 6)
////                lineChart.setVisibleXRangeMaximum(4f)
//                lineChart.invalidate()

//                if(!isRunning){
//                    lifecycle.coroutineScope.launch {
//                        val entries = ArrayList<Entry>()
//                        // Entry 배열 초기값 입력
//                        entries.add(Entry(0F, 0F))
//                        // 그래프 구현을 위한 LineDataSet 생성
//                        val dataset = LineDataSet(entries, "input")
//                        // 그래프 data 생성 -> 최종 입력 데이터
//                        val data = LineData(dataset)
//                        // chart.xml에 배치된 lineChart에 데이터 연결
//                        lineChart.data = data
//
//                        // 그래프 생성
//                        lineChart.animateXY(1, 1)
//
//                        for (i in input.indices) {
//                            delay(100)
//                            data.addEntry(Entry(i.toFloat(), input[i].toFloat()), 0)
//                            data.notifyDataChanged()
//                            lineChart.apply {
//                                notifyDataSetChanged()
//                                moveViewToX(data.entryCount.toFloat())
//                                setVisibleXRangeMaximum(4f)
//                                invalidate()
//                            }
//                        }
//                    }
//                    isRunning=false
//                }
//                coroutineClass.addData(temp)
            }
        }

        startBtn.setOnClickListener {
            if(!isRunning){
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
                        Log.d("moveViewtoX","here")
                        data.addEntry(Entry(i.toFloat(), input[i].toFloat()), 0)
                        data.notifyDataChanged()

                        lineChart.apply {
                            notifyDataSetChanged()
                            Log.d("moveViewtoX","after")
                            moveViewToX(data.entryCount.toFloat())
                            setVisibleXRangeMaximum(4f)
                            invalidate()
                        }
                    }
                }
                isRunning=false
            }
//            coroutineClass.start()

        }
    }
    private fun addEntry(x: Int, y:Int, lineChart: LineChart) {
        val data: LineData = lineChart.data
        var set = data.getDataSetByIndex(100)
//        if (set == null) {
//            set = createSet()
//            data.addDataSet(set)
//        }
        data.addEntry(Entry(x.toFloat(),y.toFloat()), 100)
        data.notifyDataChanged()
        lineChart.notifyDataSetChanged()
    }

//    inner class CoroutineClass(
//        private val lineChart: LineChart,
//        private var input: Array<Double>
//    ) {
//        private var isRunning = false
//        private var job: Job? = null
//
//        fun start() {
//            if (isRunning) return
//            isRunning = true
//
//            job = GlobalScope.launch(Dispatchers.IO) {
//                val entries = ArrayList<Entry>()
//                entries.add(Entry(0F, 0F))
//                val dataSet = LineDataSet(entries, "input")
//                val data = LineData(dataSet)
//                withContext(Dispatchers.Main) {
//                    lineChart.data = data
//                    lineChart.animateXY(1, 1)
//                }
//
//                for (i in input.indices) {
//                    delay(100)
//                    withContext(Dispatchers.Main) {
//                        data.addEntry(Entry(i.toFloat(), input[i].toFloat()), 0)
//                        data.notifyDataChanged()
//                        lineChart.notifyDataSetChanged()
//                        lineChart.moveViewToX(data.entryCount.toFloat())
//                        lineChart.setVisibleXRangeMaximum(4f)
//                        lineChart.invalidate()
//                    }
//                }
//                isRunning = false
//            }
//        }
//
//        suspend fun addData(newInput: Array<Double>) = withContext(Dispatchers.Main) {
//            input += newInput
//            val data = lineChart.data ?: return@withContext
//            val dataSet = data.getDataSetByIndex(0) ?: return@withContext
//
//            for (i in input.indices) {
//                if (i < dataSet.entryCount) {
//                    dataSet.getEntryForIndex(i).y = input[i].toFloat()
//                } else {
//                    dataSet.addEntry(Entry(i.toFloat(), input[i].toFloat()))
//                }
//            }
//
//            data.notifyDataChanged()
//            lineChart.notifyDataSetChanged()
//            lineChart.moveViewToX(data.entryCount.toFloat())
//            lineChart.setVisibleXRangeMaximum(4f)
//            lineChart.invalidate()
//        }
//    }


    override fun onResume() {
        super.onResume()
        dataClient.addListener(viewModel)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(viewModel)
    }
}