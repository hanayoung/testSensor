package com.example.testsensor.presentation

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class MeasureWorkManager (context: Context, workerParameters: WorkerParameters) : Worker(context,workerParameters) {
    override fun doWork(): Result {
        Log.d("WorkManager1", "dowork")

        val format = SimpleDateFormat("hh:mm:ss")
        val currentTime = format.format(Date())

        Log.d("MeasureWorkManager", currentTime)

        return Result.success()
    }
}