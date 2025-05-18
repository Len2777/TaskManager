package com.example.jetpacktraning.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
 fun TimeSlider(
    maxTime: Float,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Text(
                text = value.toInt().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..maxTime,
            steps = 0,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF9F66EE),
                activeTrackColor = Color(0xFF9F66EE),
                inactiveTrackColor = Color(0xFF9F66EE).copy(alpha = 0.1f)
            ),

            )
    }
}

