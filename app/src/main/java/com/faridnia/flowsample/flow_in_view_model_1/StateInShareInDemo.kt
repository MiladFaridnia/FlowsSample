package com.faridnia.flowsample.flow_in_view_model_1

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * This sample explains the difference between stateIn and shareIn operators.
 * Both operators convert a "cold" Flow into a "hot" Flow.
 *
 * StateFlow: Best for UI State. Always has a value, always replays the latest value.
 * SharedFlow: Best for Events. No initial value, configurable replay (default 0).
 */

fun main() = runBlocking {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Cold flow emitting 1, 2, 3 with 200ms gaps
    val coldFlow = flow {
        println("Cold flow: Starting execution...")
        emit(1) // T = 0ms
        delay(200) 
        emit(2) // T = 200ms
        delay(200)
        emit(3) // T = 400ms
        println("Cold flow: Completed.")
    }

    println("--- stateIn Demo ---")
    val stateFlow = coldFlow.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = -1
    )

    println("Collector 1 joining StateFlow (T=0ms)...")
    val job1 = scope.launch {
        stateFlow.collect { println("StateFlow Collector 1: $it") }
    }

    // We wait 300ms. By now, 1 and 2 have been emitted (at 0ms and 200ms).
    delay(300) 

    println("Collector 2 joining StateFlow (T=300ms)...")
    val job2 = scope.launch {
        // StateFlow ALWAYS provides the latest value (2) immediately to new collectors.
        stateFlow.collect { println("StateFlow Collector 2: $it") } 
    }

    delay(500)
    job1.cancel()
    job2.cancel()


    println("\n--- shareIn Demo ---")
    val sharedFlow = coldFlow.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1 // "Live Event" behavior: no history
    )

    println("Collector 3 joining SharedFlow (T=0ms)...")
    val job3 = scope.launch {
        sharedFlow.collect { println("SharedFlow Collector 3: $it") }
    }

    // We wait 300ms. By now, 1 and 2 have been emitted (at 0ms and 200ms).
    delay(300)

    println("Collector 4 joining SharedFlow (T=300ms)...")
    val job4 = scope.launch {
        // Since replay = 0, Collector 4 MISSES 1 and 2. 
        // It will only see 3 when it arrives at T=400ms.
        sharedFlow.collect { println("SharedFlow Collector 4: $it") }
    }

    delay(500)
    job3.cancel()
    job4.cancel()

    scope.cancel()
}
