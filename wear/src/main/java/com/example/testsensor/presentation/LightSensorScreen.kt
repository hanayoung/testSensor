package com.example.testsensor.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.SensorManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LightSensorScreen(
context: Context,
sensorManager: SensorManager,
enabled: Boolean,
onButtonClick: () -> Unit
) {
        val viewModel: LightSensorViewModel = viewModel(
        factory = lightSensorViewModelFactory(
            sensorManager=sensorManager
            )
        )
        // on below line we are specifying theme as scaffold.
        Scaffold(
            // in scaffold we are specifying top bar.
            topBar = {
                // inside top bar we are specifying background color.
                TopAppBar(
                    // along with that we are specifying title for our top bar.
                    title = {
                        // in the top bar we are specifying tile as a text
                        Text(
                            // on below line we are specifying
                            // text to display in top app bar.
                            text = "Proximity Sensor Example",

                            // on below line we are specifying
                            // modifier to fill max width.
                            modifier = Modifier.fillMaxWidth(),

                            // on below line we are
                            // specifying text alignment.
                            textAlign = TextAlign.Center,

                            // on below line we are
                            // specifying color for our text.
                            color = Color.White
                        )
                    }
                )
            }
        ) {
            // on below line we are calling proximity
            // sensor method to use proximity sensor.
//            Button(
//                modifier = Modifier.fillMaxWidth(0.5f),
//                onClick = {
//                    Log.d("permission","is granted")
//                    onButtonClick()
//                }
//            ) {
//                val buttonTextId = if (enabled) {
//                    R.string.stop
//                } else {
//                    R.string.start
//                }
//                Text(stringResource(buttonTextId))
//            }


                LightSensor(context = context)


        }
}