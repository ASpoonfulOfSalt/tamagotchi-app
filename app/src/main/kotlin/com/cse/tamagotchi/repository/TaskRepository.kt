package com.cse.tamagotchi.repository

import android.content.Context
import com.cse.tamagotchi.data.TaskDao
import com.cse.tamagotchi.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val jsonFormat = Json {
    ignoreUnknownKeys = true
    prettyPrint = false
}

class TaskRepository(
    private val context: Context,
    private val taskDao: TaskDao
) {
    val tasks: Flow<List<Task>> = taskDao.getAllTasks().map { tasks ->
        tasks.sortedBy { it.isDaily }
    }

    @Serializable
    private data class TaskList(val tasks: List<Task>)

    private fun loadAllPossibleTasks(): List<Task> {
        return try {
            val jsonText = context.assets.open("tasks.json").bufferedReader().use { it.readText() }

            val parsed = jsonFormat.decodeFromString<TaskList>(jsonText)

            parsed.tasks
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun completeTask(taskId: String): Task? {
        val task = tasks.first().find { it.id == taskId }
        if (task != null && !task.isCompleted) {
            val updatedTask = task.copy(isCompleted = true)
            taskDao.updateTask(updatedTask)
            return updatedTask
        }
        return null
    }

    suspend fun resetDailyTasks() {
        taskDao.deleteDailyTasks()
        loadNewDailyTasks()
    }

    suspend fun resetWeeklyTasks() {
        taskDao.deleteWeeklyTasks()
        loadNewWeeklyTasks()
    }

    suspend fun loadSampleTasks() {
        if (tasks.first().isEmpty()) {
            loadNewDailyTasks()
            loadNewWeeklyTasks()
        }
    }

    private suspend fun loadNewDailyTasks() {
        val allTasks = loadAllPossibleTasks()
        val newDailyTasks = allTasks.filter { it.isDaily }.shuffled().take(4)
        taskDao.insertAll(newDailyTasks)
    }

    private suspend fun loadNewWeeklyTasks() {
        val allTasks = loadAllPossibleTasks()
        val newWeeklyTasks = allTasks.filter { !it.isDaily }.shuffled().take(2)
        taskDao.insertAll(newWeeklyTasks)
    }
}
