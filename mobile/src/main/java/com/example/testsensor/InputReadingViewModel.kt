package com.example.testsensor

import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InputReadingViewModel(private val healthConnectClient: HealthConnectClient) :ViewModel(){
    val permissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
    )
    private val _currentPermissions = MutableLiveData(false)
    val currentPermissions : LiveData<Boolean>
    get() = _currentPermissions

    val permissionsLauncher = requestPermissionsActivityContract()

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    }

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }
    suspend fun tryWithPermissionsCheck(block:suspend ()-> Unit){
        _currentPermissions.value = hasAllPermissions(permissions)
    }
    fun initialLoad(){
        viewModelScope.launch {
            tryWithPermissionsCheck {

            }
        }
    }
}