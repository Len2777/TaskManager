package com.example.jetpacktraning.ui.theme
import kotlin.collections.List

data class Tasks(val name: String, val hours: Int, val minutes: Int)


object TaskRepository {
    val tasks = mutableListOf<Tasks>()

    fun addTask(task: Tasks) {

        tasks.add(0,task)
    }


    fun getAllTasks(): List<Tasks> = tasks


}

