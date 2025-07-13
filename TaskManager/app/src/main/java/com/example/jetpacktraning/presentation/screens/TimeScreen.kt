package com.example.jetpacktraning.presentation.screens

import androidx.compose.foundation.Canvas
import com.example.jetpacktraning.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
fun Time_Screen(task: Tasks, onBack: () -> Unit) {
    val isRunning = remember { mutableStateOf(false) }

    TopBar(task = task, onBack = onBack)
    CircularTimer(task, isRunning)
}


@Composable
fun TopBar(task: Tasks, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopStart,
        )

    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {

            IconButton(
                onClick = { onBack() },
                modifier = Modifier.width(55.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()

                )
            }

            Box( contentAlignment = Alignment.TopCenter ) {
                Text(
                    text = task.name,
                    color = Color.White,
                    fontSize = 30.sp,
                    )
            }
        }
    }
}

@Composable
fun CircularTimer(task: Tasks, isRunning: MutableState<Boolean>) {
    val durationMillis = task.minutes * 60_000 + task.hours * 3_600_000
    var currentState = remember { mutableStateOf(0f) }

    val activeBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9F66EE),
            Color(0xFF5532A1)
        ),
    )

    val inActiveColor = Color(0xFF1B143F)
    val strokeWidth = 16.dp

    var currentTime by remember { mutableStateOf(durationMillis) }
    var value by remember { mutableStateOf(1f) }

    LaunchedEffect(isRunning.value) {
        if (isRunning.value) {
            if (currentTime <= 0L) {
                currentTime = durationMillis
            }
            while (currentTime > 0 && isRunning.value) {
                delay(100)
                currentTime -= 100
                value = currentTime / durationMillis.toFloat()
            }
            if (currentTime <= 0L) {
                isRunning.value = false
                currentState.value = 2f
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(contentAlignment = Alignment.Center) {
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

            val timeText = if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }

            val txtSize = if (hours > 0) 44.sp else 50.sp

            Text(
                text = timeText,
                fontSize = txtSize,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        ButtonsAtBottom(
            currentState = currentState,
            isRunning = isRunning,
            onStart = { isRunning.value = true },
            onStop = { isRunning.value = false },
            onQuit = {
                currentTime = durationMillis
                value = 1f
            }
        )
    }
}


@Composable
fun StopButton(onStop: () -> Unit) {


    IconButton(
        onClick = { onStop() },
        Modifier.size(64.dp)
    ) {


        Image(
            painter = painterResource(id = R.drawable.stop),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()

        )

    }
}

@Composable
fun StartButton(onStart: () -> Unit) {
        IconButton(
            onClick = { onStart() },
            modifier = Modifier.size(64.dp)
        ) {

        Image(
            painter = painterResource(id = R.drawable.play),
            contentDescription = null,

            modifier = Modifier.fillMaxSize()

        )

    }
}

@Composable
fun QuitButton(onQuit: () -> Unit) {
    IconButton(
        onClick = { onQuit() },
        modifier = Modifier.size(64.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.quit),
            contentDescription = null,

            modifier = Modifier.fillMaxSize()

        )

    }
}


@Composable
fun ButtonsAtBottom(
    currentState: MutableState<Float>,
    isRunning: MutableState<Boolean>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onQuit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            var text by remember {mutableStateOf("")}

            if (isRunning.value) {
                StopButton(onStop = onStop)
                text = "Pause"
            } else {
                StartButton(onStart = onStart)
                text = "Start"
            }

            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 14.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            QuitButton(onQuit = {
                onStop()
                onQuit()

            })


            Text(
                text = "Quit",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
    }
}

