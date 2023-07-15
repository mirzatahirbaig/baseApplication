package com.mobizion.xbase.view.model

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */
class PermissionViewModel:ViewModel() {
    private val _singlePermissionStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val singlePermissionStatus: LiveData<Boolean>
        get() = _singlePermissionStatus

    fun setSinglePermissionStatus(status: Boolean)  = viewModelScope.launch {
        _singlePermissionStatus.value = status
    }

    private val _multiplePermissionStatus: MutableLiveData<Map<String, Boolean>> by lazy {
        MutableLiveData()
    }

    val multiplePermissionStatus: LiveData<Map<String, Boolean>>
        get() = _multiplePermissionStatus

    fun setMultiplePermissionStatus(status: Map<String,Boolean>) = viewModelScope.launch {
        _multiplePermissionStatus.value = status
    }

    private val _activityResult: MutableLiveData<ActivityResult> by lazy {
        MutableLiveData()
    }

    val activityResult: LiveData<ActivityResult?>
        get() = _activityResult

    fun setActivityResult(result: ActivityResult) = viewModelScope.launch {
        _activityResult.value = result
    }

    fun resetValue() {
        _activityResult.value = null
    }

}