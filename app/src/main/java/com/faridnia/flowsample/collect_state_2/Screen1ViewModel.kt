package com.faridnia.flowsample.collect_state_2

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
            println("flow is active $currentTime")
            emit(currentTime++)
        }
    }
        /**
         * stateIn Convert normal flow to stateflow
         * It caches the latest value
         * */
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily, // Flow will start as soon s first collector appears and never stops until scope is canceled
            initialValue = 0
        )
}