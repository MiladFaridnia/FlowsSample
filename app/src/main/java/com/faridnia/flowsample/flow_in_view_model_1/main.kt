package com.faridnia.flowsample.flow_in_view_model_1

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//Run in https://play.kotlinlang.org/


fun main() = runBlocking {
    val myVm = MyViewModel()
    myVm.loadData()
	myVm.loadData()

    delay(2000)
}



class MyViewModel /*: ViewModel() */ {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun loadData() {
        println("Load Data " + state.value)

        GlobalScope.launch {
            getDataFlow()
                .onStart {
                    _state.value = UiState(isLoading = true)
                    println("onStart " + state.value)

                }
                .catch { e ->
                    _state.value = UiState(error = e.message)
                    println("catch " + state.value)
                }
                .collect { result ->
                    _state.value = UiState(data = result)
                    println("collect " + state.value)
                }
        }
    }

    private fun getDataFlow(): Flow<List<String>> = flow {
        delay(1000)
        println("emmiting items " + state.value)
        emit(listOf("Item 1", "Item 2", "Item 3"))
        println("after emmiting items " + state.value)

    }
}

data class UiState(
    val isLoading: Boolean = false,
    val data: List<String> = emptyList(),
    val error: String? = null
)