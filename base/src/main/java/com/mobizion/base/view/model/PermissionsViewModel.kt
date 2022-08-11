/**
 * created by tahir baig
 * 3 march 2022
 */

package com.mobizion.base.view.model

import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionsViewModel: ViewModel() {

    private val _contactPermissionStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val contactPermissionStatus: LiveData<Boolean>
        get() = _contactPermissionStatus

    fun setContactPermissionStatus(status: Boolean) {
        _contactPermissionStatus.value = status
    }

    private val _cameraPermissionStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val cameraPermissionStatus: LiveData<Boolean>
        get() = _cameraPermissionStatus

    fun setCameraPermissionStatus(status: Boolean) {
        _cameraPermissionStatus.value = status
    }

    private val _storagePermissionStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val storagePermissionStatus: LiveData<Boolean>
        get() = _storagePermissionStatus

    fun setStoragePermissionStatus(status: Boolean) {
        _storagePermissionStatus.value = status
    }

    private val _microphonePermissionStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val microphonePermissionStatus: LiveData<Boolean>
        get() = _microphonePermissionStatus

    fun setMicrophonePermissionStatus(status: Boolean) {
        _microphonePermissionStatus.value = status
    }

    private val _multiplePermissionStatus: MutableLiveData<Map<String,Boolean>> by lazy {
        MutableLiveData()
    }

    val multiplePermissionStatus: LiveData<Map<String,Boolean>>
        get() = _multiplePermissionStatus

    fun setMultiplePermissionStatus(status: Map<String,Boolean>) {
        _multiplePermissionStatus.value = status
    }

    private val _activityResult: MutableLiveData<ActivityResult> by lazy {
        MutableLiveData()
    }

    val activityResult: LiveData<ActivityResult>
        get() = _activityResult

    fun setActivityResult(result: ActivityResult) {
        _activityResult.value = result
    }


    private val _locationPermissionStatus: MutableLiveData<Map<String,Boolean>> by lazy {
        MutableLiveData()
    }

    val locationPermissionStatus: LiveData<Map<String,Boolean>>
        get() = _locationPermissionStatus

    fun setLocationPermissionStatus(status: Map<String,Boolean>) {
        _locationPermissionStatus.value = status
    }

}