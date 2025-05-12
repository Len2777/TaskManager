package com.example.jetpacktraning

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jetpacktraning.ui.theme.Tasks
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.taskDataStore by preferencesDataStore(name = "tasks")

object TaskStorage {
    private val TASKS_KEY = stringPreferencesKey("task_list")
    private val gson = Gson()

     suspend fun saveTasks(context: Context, tasks: List<Tasks>) {
        val json = gson.toJson(tasks)
        context.taskDataStore.edit { preferences ->
            preferences[TASKS_KEY] = json
        }
    }

    fun getTasks(context: Context): Flow<List<Tasks>> {
        return context.taskDataStore.data.map { preferences ->
            val json = preferences[TASKS_KEY] ?: return@map emptyList()
            val type = object : TypeToken<List<Tasks>>() {}.type
            gson.fromJson(json, type)
        }
    }
}
