package com.faridnia.flowsample.collect_state_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faridnia.flowsample.ui.theme.FlowSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowSampleTheme {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = "screen1"
                ) {
                    composable(route = "screen1") {
                        val viewModel = viewModel<Screen1ViewModel>()
                        val time by viewModel.timer.collectAsStateWithLifecycle()
                        Screen1(
                            time = time,
                            onButtonClick = {
                                navController.navigate("screen2")
                            }
                        )
                    }
                    composable(route = "screen2") {
                        Screen2(1)

                    }
                }
            }
        }
    }
}

@Composable
fun Screen1(time: Int, onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello $time!",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onButtonClick() }) {
            Text(
                text = "Go",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun Screen2(time: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scren 2 $time!",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }


}


@Preview(showBackground = true)
@Composable
fun Screen1Preview() {
    FlowSampleTheme {
        Screen1(
            time = 1,
            onButtonClick = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Screen2Preview() {
    FlowSampleTheme {
        Screen2(
            time = 1
        )
    }
}