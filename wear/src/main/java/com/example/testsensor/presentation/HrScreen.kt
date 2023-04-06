package com.example.testsensor.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.DataClient

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HrScreen(
    context: Context
) {
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
        HrSensor(context = context)
    }
}