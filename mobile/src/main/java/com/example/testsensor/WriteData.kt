package com.example.testsensor

import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


private lateinit var date1: Date
private lateinit var date2: Date
private var beatsPerMinutes: Long = 0
private lateinit var end_time_heart: Instant
private lateinit var start_time_heart: Instant

suspend fun writeData(healthConnectClient: HealthConnectClient){
    Log.d("writeData","I m in!")
    val heartRateRecord =
        HeartRateRecord(
            startTime = ZonedDateTime.now().toInstant(),
            startZoneOffset = ZonedDateTime.now().offset,
            endTime = ZonedDateTime.now().toInstant(),
            endZoneOffset = ZonedDateTime.now().offset,
            // records 10 arbitrary data, to replace with actual data
            samples =
            List(10) { index ->
                HeartRateRecord.Sample(
                    time = ZonedDateTime.now().toInstant() + Duration.ofSeconds(index.toLong()),
                    beatsPerMinute = 100 + index.toLong(),
                )
            },
        )
    Log.d("writeData",heartRateRecord.toString())
    healthConnectClient.insertRecords(listOf(heartRateRecord))

}
suspend fun readHeartRatesByTimeRangeDay(
    healthConnectClient: HealthConnectClient,
    startTime: LocalDateTime,
    endTime: LocalDateTime
){
    Log.d("readData","here")
    val response =
        healthConnectClient.readRecords(
            ReadRecordsRequest(
                HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        )
    Log.d("readData",response.records.toString())
    for(heartRecord in response.records){
        val series = heartRecord.samples
        Log.d("readData",series.toString())
        series.forEach{
                serie ->
            beatsPerMinutes = serie.beatsPerMinute
        }

        start_time_heart = heartRecord.startTime
        end_time_heart = heartRecord.endTime
        date1 = Date.from(start_time_heart)
        date2 = Date.from(end_time_heart)

        val formatter = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        val formattedDate1 = formatter.format(date1)
        val formattedDate2 = formatter.format(date2)
        Log.d("readData","$formattedDate1  $formattedDate2  $beatsPerMinutes")


    }
}
suspend fun hasAllPermissions(permissions: Set<String>,healthConnectClient: HealthConnectClient): Boolean {
    return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
}

fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
    return PermissionController.createRequestPermissionResultContract()
}