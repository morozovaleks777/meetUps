package com.example.dwkidsandroid.presentation.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _sharedData = MutableStateFlow<Map<String, Any?>>(emptyMap())
    val sharedData = _sharedData.asStateFlow()

    fun <T> shareData(key: String, data: T) {
        _sharedData.value = _sharedData.value.toMutableMap().apply { put(key, data) }
    }

    fun <T> shareData(vararg data : Pair<String, T>) {
        data.forEach { (key, value) ->  shareData(key, value) }
    }

    fun <T> getSharedData(key: String): T? {
        return _sharedData.value[key]?.let {
            try {
                it as T
            } catch (e: Throwable) {
                Timber.e(e)
                null
            }
        }
    }
}