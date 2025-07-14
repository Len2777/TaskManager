package com.example.jetpacktraning.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpacktraning.R
import com.example.jetpacktraning.data.TaskStorage
import com.example.jetpacktraning.domain.model.Tasks
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ManagerScreen() {
    var currentState by remember { mutableStateOf(1) }
    var selectedTask by remember { mutableStateOf<Tasks?>(null)}

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val taskFlow = TaskStorage.getTasks(context).collectAsState(initial = emptyList())
    val allTasks = taskFlow.value
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topPadding = (screenHeight * 0.08f).dp.coerceIn(10.dp, 100.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .padding(top = topPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        )  {
                Text(
                    text = "Main Task",
                    color = Color.White,
                    fontSize = 30.sp,
                )
                allTasks.firstOrNull()?.let { firstTask ->
                    RaceProjectButton(task = firstTask, onClick = { onNext(firstTask) })
                }
            }


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

            allTasks.forEach { task ->
                key(task.id) {
                    SwipeToDeleteContainer(item = task, onDelete = {
                        scope.launch {
                            val updatedTasks = allTasks.filter { it.id != task.id }
                            TaskStorage.saveTasks(context, updatedTasks)
                        }
                    }) {
                        TaskButton(task = task, onClick = { onNext(task) })
                    }
                }
            }
        }
    }
}



@Composable
fun RaceProjectButton(task: Tasks, onClick: () -> Unit) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF9F66EE), Color(0xFF5532A1))
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "ButtonScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale).border(
                width = 2.dp,
                brush = gradientBrush,
                shape = RoundedCornerShape(24.dp)
            )
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color(0xFF1C1C2A), shape = RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(brush = gradientBrush, shape = RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.starttask),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%02d:%02d:00", task.hours, task.minutes),
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun TaskButton(task: Tasks, onClick: () -> Unit) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF9F66EE), Color(0xFF5532A1))
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "ButtonScale"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color(0xFF1C1C2A), shape = RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(brush = gradientBrush, shape = RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.starttask),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%02d:%02d:00", task.hours, task.minutes),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )
    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved){
            delay(animationDuration.toLong())
            onDelete(item)
        }


    }


    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()

    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(swipeToDismissBoxState = state)
            },


        ) {
            content(item)
        }

    }
}

@Composable
fun DeleteBackground(
    swipeToDismissBoxState: SwipeToDismissBoxState
) {
    val color = if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )


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