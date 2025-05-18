package com.example.jetpacktraning.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun NextTaskButton(text: String, onNext: () -> Unit) {
    val isTextEmpty = text.isEmpty()
    Button(
        onClick = onNext,
        enabled = !isTextEmpty,
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isTextEmpty) Color(0xFF444444) else Color(0xFF1C1C2A)
        )
    ) {
        Text("Next", color = if (isTextEmpty) Color.Gray else Color.White)
    }
}

@Composable
fun CreateTaskButton(hours : Float , minutes: Float,  onCreate: () -> Unit) {
    var state = false
    if(minutes >= 1 || hours >= 1){
        state = true
    }

    Button(
        onClick = onCreate,
        enabled = state,
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (!state) Color(0xFF444444) else Color(0xFF1C1C2A)
        )
    ) {
        Text("Create", color = if (!state) Color.Gray else Color.White)
    }
}

@Composable
fun GeneralButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(text = text, color = Color(0xFF888C9F))
    }
}




