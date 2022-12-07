package com.mobizion.xbaseprefrences.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizion.xbaseprefrences.abstraction.DataStoreRepository
import kotlinx.coroutines.launch

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 12/09/2022
 */

class DataStoreViewModel(private val repository: DataStoreRepository) : ViewModel() {

    fun put(key: String, value: String) = viewModelScope.launch {
        repository.put(key, value)
    }


    fun put(key: String, value: Int) = viewModelScope.launch {
        repository.put(key, value)
    }


    fun put(key: String, value: Boolean) = viewModelScope.launch {
        repository.put(key, value)
    }



    private val _stringValue:MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val stringValue:LiveData<String>
    get() = _stringValue


    fun getString(key: String) = viewModelScope.launch {
        _stringValue.value = repository.getString(key)
    }

    private val _intValue:MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    val intValue:LiveData<Int>
        get() = _intValue

    fun getInt(key: String) = viewModelScope.launch {
        _intValue.value = repository.getInt(key)
    }

    private val _booleanValue:MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val booleanValue:LiveData<Boolean>
        get() = _booleanValue

    fun getBoolean(key: String) = viewModelScope.launch {
        _booleanValue.value = repository.getBoolean(key)
    }

    fun clearData() = viewModelScope.launch {
        repository.clearData()
    }

}