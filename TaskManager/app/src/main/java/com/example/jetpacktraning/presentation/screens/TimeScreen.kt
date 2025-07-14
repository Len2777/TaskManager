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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpacktraning.domain.model.Tasks
import com.example.jetpacktraning.domain.model.TimerViewModel



@Composable
fun Time_Screen(task: Tasks, onBack: () -> Unit) {
    val viewModel: TimerViewModel = viewModel(key = "task_${task.id}") {
        TimerViewModel()
    }
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()

    val totalMillis = remember(task) {
        task.minutes * 60_000L + task.hours * 3_600_000L
    }

    LaunchedEffect(task.id) {
        viewModel.initTimer(totalMillis)
    }

    TopBar(task = task, onBack = onBack)

    TimerBody(
        timeLeft = timeLeft,
        isRunning = isRunning,
        totalDuration = totalMillis,
        onStart = { viewModel.startTimer(timeLeft) },
        onPause = { viewModel.pauseTimer() },
        onReset = { viewModel.resetTimer() }
    )
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
fun TimerBody( timeLeft: Long, isRunning: Boolean, totalDuration: Long, onStart: () -> Unit, onPause: () -> Unit, onReset: () -> Unit){
    val activeBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9F66EE),
            Color(0xFF5532A1)
        ),
    )

    val inActiveColor = Color(0xFF1B143F)
    val strokeWidth = 16.dp

    val value = timeLeft.toFloat() / totalDuration.toFloat()

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

            val totalSeconds = timeLeft / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60

            val timeText = if (hours > 0)
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            else
                String.format("%02d:%02d", minutes, seconds)

            val txtSize = if (hours > 0) 44.sp else 50.sp

            Text(
                text = timeText,
                fontSize = txtSize,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        ButtonsAtBottom(
            isRunning = isRunning,
            onStart = onStart,
            onStop = onPause,
            onReset = onReset
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
fun QuitButton(onReset: () -> Unit) {
    IconButton(
        onClick = { onReset() },
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
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
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

            if (isRunning) {
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
            QuitButton(onReset = {
                onStop()
                onReset()

            })


            Text(
                text = "Reset",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
    }
}

