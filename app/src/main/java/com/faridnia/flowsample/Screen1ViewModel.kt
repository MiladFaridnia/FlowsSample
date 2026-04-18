package com.faridnia.flowsample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class Screen1ViewModel : ViewModel() {

    private var currentTime = 0
    val timer = flow {
        while (true) {
            delay(1000L)
            println("flow is active")
            emit(currentTime++)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = 0
    )
}