package com.example.testsensor

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlin.concurrent.thread


















//private fun setChart(lineChart: LineChart){
//    val xAxis : XAxis = lineChart.xAxis
//
//    xAxis.apply {
//        position=XAxis.XAxisPosition.BOTTOM
//        textSize=10f
//        setDrawGridLines(false)
//        granularity=1f
//        axisMinimum=2f
//        isGranularityEnabled = true
//    }
//    lineChart.apply {
//        axisRight.isEnabled = false
//        axisLeft.axisMaximum = 50f
//        legend.apply {
//            textSize=15f
//            verticalAlignment= Legend.LegendVerticalAlignment.TOP
//            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//            orientation = Legend.LegendOrientation.HORIZONTAL
//            setDrawInside(false)
//        }
//    }
//    val lineData = LineData()
//    lineChart.data=lineData
//    feedMultiple()
//
//}
//
//
//private fun feedMultiple(lineChart: LineChart){
////    if(thread !== null){
////        thread!!.interrupt()
////    }
//    val runnable = Runnable{
//        addEntry(lineChart =lineChart )
//    }
//    Thread(Runnable {
//        while (true){
//            runOnUiThread(runnable)
//            try {
//                Thread.sleep(1000)
//            }
//            catch (e: InterruptedException){
//                e.printStackTrace()
//            }
//        }
//    })!!.start()
//}
//
//
//private fun addEntry(lineChart: LineChart){
//    val data : LineData = lineChart.data
//
//    data ?.let{
//        var set : ILineDataSet? = data.getDataSetByIndex(0)
//
//        if(set == null){
//            set = createSet()
//
//        }
//    }
//}
