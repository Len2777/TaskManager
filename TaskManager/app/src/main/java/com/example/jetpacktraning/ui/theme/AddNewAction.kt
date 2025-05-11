package com.example.jetpacktraning.ui.theme


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpacktraning.R


@Composable
fun AddNewActionSection() {
    var currentStep by remember { mutableStateOf(1) }

    var sectionName by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf(0f) }
    var minutes by remember { mutableStateOf(0f) }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (currentStep) {
            1 -> FirstStep(
                onNext = { currentStep = 2 }
            )

            2 -> SecondStep(
                text = sectionName,
                onTextChange = { sectionName = it },
                onNext = { currentStep = 3 },
                onCancel = { currentStep = 1 }
            )

            3 -> ThirdStep(
                hours = hours,
                minutes = minutes,
                onHoursChange = { hours = it },
                onMinutesChange = { minutes = it },
                onCreate = {
                    TaskRepository.addTask(Tasks(sectionName, hours.toInt(), minutes.toInt()))

                    currentStep = 1
                },
                onBack = { currentStep = 2 }
            )

        }



    }
}



@Composable
private fun FirstStep(
    onNext: () -> Unit
) {
    var value by remember { mutableStateOf(1f) }

    val activeBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9F66EE),
            Color(0xFF5532A1)
        )
    )
    val inActiveColor = Color(0xFF1B143F)
    val strokeWidth = 7.dp
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .clickable(onClick = onNext),
            contentAlignment = Alignment.Center
        ) {
            Canvas(Modifier.size(220.dp)) {


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

            Image(
                painter = painterResource(id = com.example.jetpacktraning.R.drawable.baseline_add_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(40.dp),
                colorFilter = ColorFilter.tint(Color(0xFF9F66EE))

            )

            Text(
                text = "Add Action",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 50.dp)
            )

        }
    }
}

@Composable
private fun SecondStep(
    text: String,
    onTextChange: (String) -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val isTextEmpty = text.isEmpty()

    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.Center
    ) {


        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    "Section name",
                    textAlign = TextAlign.Start,
                    color = Color(0xFF888C9F),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .height(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF1C1C2A)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.White,
                focusedIndicatorColor = Color(0xFF9F66EE),
                unfocusedIndicatorColor = Color.Transparent

            )
        )
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 70.dp)
            .clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center,

        ) {
        Canvas(
            Modifier
                .fillMaxWidth(0.8f)
                .height(30.dp)
        ) {

            val fullWidth = size.width
            val progress = 0.5f

            drawLine(
                color = Color(0xFF1C1C2A),
                start = Offset(0f, size.height / 2),
                end = Offset(fullWidth, size.height / 2),
                strokeWidth = 30f,
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color(0xFF9F66EE),
                start = Offset(0f, size.height / 2),
                end = Offset(fullWidth * progress, size.height / 2),
                strokeWidth = 30f,
                cap = StrokeCap.Round
            )
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 170.dp),
        contentAlignment = Alignment.Center
    ) {

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
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 280.dp),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text("Cancel", color = Color(0xFF888C9F))
        }
    }
}


@Composable
private fun ThirdStep(
    hours: Float,
    minutes: Float,
    onHoursChange: (Float) -> Unit,
    onMinutesChange: (Float) -> Unit,
    onCreate: () -> Unit,
    onBack: () -> Unit
) {
    var value by remember { mutableStateOf(1f) }

    val activeBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9F66EE),
            Color(0xFF5532A1)
        )
    )
    val inActiveColor = Color(0xFF1B143F)
    val strokeWidth = 7.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 96.dp)
            .navigationBarsPadding(),

        ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.time),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF9F66EE))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Set time for task",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(140.dp),
                contentAlignment = Alignment.Center
            ) {


                Canvas(Modifier.size(140.dp)) {
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
                        sweepAngle = 240f * value,
                        useCenter = false,
                        style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = String.format("%02d:%02d", hours.toInt(), minutes.toInt()),
                    fontSize = 30.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp)
                ) {

                    TimeSlider(
                        maxTime = 23f,
                        label = "Hours",
                        value = hours,
                        onValueChange = onHoursChange,

                        )

                    Spacer(modifier = Modifier.height(24.dp))


                    TimeSlider(
                        maxTime = 59f,
                        label = "Minutes",
                        value = minutes,
                        onValueChange = onMinutesChange,

                        )


                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onCreate,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1C1C2A)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Create", color = Color.White)
            }


            Button(
                onClick = onBack,


                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF888C9F)
                ),

                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Back",
                    fontSize = 15.sp,

                    )
            }
        }


    }
}

@Composable
private fun TimeSlider(
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

