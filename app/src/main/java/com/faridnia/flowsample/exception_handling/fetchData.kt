package com.faridnia.flowsample.exception_handling

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking {
    loadData()
    delay(2000)
}

fun emmitData(): Flow<String> = flow {
    emit("data 1")
    delay(500)
    emit("data 2")
    delay(500)
    throw RuntimeException("Exception occurred")
    delay(500)
    emit("data 3") //This will never emit
}


fun loadData() {
    println("Load Data ")

    GlobalScope.launch {
        emmitData()
            .map {
                println("map: $it")
                it.uppercase()
            }  // Exception won't happen here
            .onEach {
                println("onEach: $it")
            }
            .onCompletion { cause ->
                println("onCompletion : " + cause?.message)
            }
            .catch { e ->
                println("Error: ${e.message}")
            }.collect {
                println("collect: $it")
            }


    }
}