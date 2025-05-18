package com.example.jetpacktraning.presentation.screens

import androidx.compose.foundation.Canvas
import com.example.jetpacktraning.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jetpacktraning.domain.model.Tasks
import kotlinx.coroutines.delay


@Composable
fun Time_Screen(task : Tasks, onBack: () -> Unit) {

    TopBar(task = task, onBack = onBack)
    CircularTimer(task)
}


@Composable
fun TopBar(task : Tasks, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp).navigationBarsPadding(),
        contentAlignment = Alignment.TopStart,


    )

    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = {onBack()},
                modifier = Modifier.width(40.dp)) {

            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = null,
                modifier = Modifier.width(40.dp)

            ) }

            Text(
                text = task.name,
                color = Color.White,
                fontSize = 30.sp,


                )

            Image(
                painter = painterResource(id = R.drawable.tag_image),
                contentDescription = null,
                modifier = Modifier.width(40.dp)

            )


        }


    }


}

@Composable
fun CircularTimer(task: Tasks) {
    val durationMillis = task.minutes * 60_000 + task.hours * 3_600_000

    val activeBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9F66EE),
            Color(0xFF5532A1)
        ),
    )

    val inActiveColor = Color(0xFF1B143F)
    val strokeWidth = 12.dp

    var currentTime by remember { mutableStateOf(durationMillis) }
    var value by remember { mutableStateOf(1f) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (currentTime > 0) {
                delay(100)
                currentTime -= 100
                value = currentTime / durationMillis.toFloat()
            }
            isRunning = false
        }
    }

    Box(
        Modifier.fillMaxSize().padding(bottom = 64.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(Modifier.size(240.dp)) {
            drawArc(
                color = inActiveColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                brush = activeBrush,
                startAngle = -90f,
                sweepAngle = 360f * value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        val totalSeconds = currentTime / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        Text(
            text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(onClick = {
            if (!isRunning) {
                currentTime = durationMillis
                value = 1f
            }
            isRunning = !isRunning
        }) {
            Text(if (isRunning) "Pause" else "Start")
        }
    }
}
