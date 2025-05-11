package com.example.jetpacktraning.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpacktraning.R


@Composable
fun ManagerScreen() {
    var currentState by remember { mutableStateOf(1) }
    var selectedTask by remember { mutableStateOf<Tasks?>(null) }

    when (currentState) {
        1 -> TaskSection(
            onNext = { task ->
                selectedTask = task
                currentState = 2
            }
        )

        2 -> selectedTask?.let { task ->
            Time_Screen(task = task, onBack = { currentState = 1 })
        }
    }


}



@Composable
fun TaskSection(
    onNext: (Tasks) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val tasks = TaskRepository.getAllTasks()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .align(Alignment.TopStart)
                .padding(top = LocalConfiguration.current.screenHeightDp.dp / 3),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Tasks",
                color = Color.White,
                fontSize = 30.sp,
            )

            tasks.forEach { task ->
                Button(
                    onClick = { onNext(task) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1C1C2A)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFF9F66EE), shape = RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.starttask),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = task.name, color = Color.White, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = String.format("%02d:%02d:00", task.hours, task.minutes),
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))


                        }
                    }
                }
            }

        }
    }
}


//@Composable
//fun TaskBadge(text: String, backgroundColor: Color) {
//    Box(
//        modifier = Modifier
//            .background(backgroundColor, shape = RoundedCornerShape(6.dp))
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//    ) {
//        Text(text = text, color = Color.White, fontSize = 12.sp)
//    }
//}