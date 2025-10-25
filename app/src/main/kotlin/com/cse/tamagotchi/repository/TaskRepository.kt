package com.cse.tamagotchi.repository

import com.cse.tamagotchi.data.TaskDao
import com.cse.tamagotchi.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {
    val tasks: Flow<List<Task>> = taskDao.getAllTasks().map { tasks ->
        tasks.sortedBy { it.isDaily }
    }

    private val allPossibleTasks = listOf(
        Task(title = "Drink Water", isDaily = true, currencyReward = 5),
        Task(title = "Go for a Walk", isDaily = true, currencyReward = 15),
        Task(title = "Study Kotlin", isDaily = true, currencyReward = 25),
        Task(title = "Read a Book", isDaily = true, currencyReward = 20),
        Task(title = "Brush Teeth", isDaily = true, currencyReward = 5),
        Task(title = "Make Your Bed", isDaily = true, currencyReward = 5),
        Task(title = "Eat a Healthy Meal", isDaily = true, currencyReward = 10),
        Task(title = "Take a Short Walk", isDaily = true, currencyReward = 10),
        Task(title = "Stretch for 5 minutes", isDaily = true, currencyReward = 10),
        Task(title = "Write in Journal", isDaily = true, currencyReward = 15),
        Task(title = "Plan Tomorrow’s Tasks", isDaily = true, currencyReward = 15),
        Task(title = "Avoid Social Media for 1 Hour", isDaily = true, currencyReward = 20),
        Task(title = "Compliment Someone", isDaily = true, currencyReward = 10),
        Task(title = "Take 10 Deep Breaths", isDaily = true, currencyReward = 5),
        Task(title = "Review Budget/Expenses", isDaily = true, currencyReward = 20),
        Task(title = "Go to Bed Before 11 PM", isDaily = true, currencyReward = 20),
        Task(title = "Cook a Homemade Meal", isDaily = true, currencyReward = 25),
        Task(title = "Exercise for 15 minutes", isDaily = true, currencyReward = 30),
        Task(title = "Meditate for 5 minutes", isDaily = true, currencyReward = 10),
        /* Weekly Tasks */
        Task(title = "Weekly: Clean Room", isDaily = false, currencyReward = 50),
        Task(title = "Weekly: Do Laundry", isDaily = false, currencyReward = 40),
        Task(title = "Weekly: Grocery Shopping", isDaily = false, currencyReward = 60),
        Task(title = "Weekly: Meal Prep for the Week", isDaily = false, currencyReward = 60),
        Task(title = "Weekly: Call a Friend or Family Member", isDaily = false, currencyReward = 30),
        Task(title = "Weekly: Declutter One Area", isDaily = false, currencyReward = 45),
        Task(title = "Weekly: Deep Clean Kitchen", isDaily = false, currencyReward = 50),
        Task(title = "Weekly: Water All Plants", isDaily = false, currencyReward = 30),
        Task(title = "Weekly: Backup Important Files", isDaily = false, currencyReward = 25),
        Task(title = "Weekly: Donate or Recycle Items", isDaily = false, currencyReward = 40),
        Task(title = "Weekly: Unplug for a Day (No Screens)", isDaily = false, currencyReward = 70),
        )

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
        val newDailyTasks = allPossibleTasks.filter { it.isDaily }.shuffled().take(4)
        taskDao.insertAll(newDailyTasks)
    }

    private suspend fun loadNewWeeklyTasks() {
        val newWeeklyTasks = allPossibleTasks.filter { !it.isDaily }.shuffled().take(2)
        taskDao.insertAll(newWeeklyTasks)
    }
}
