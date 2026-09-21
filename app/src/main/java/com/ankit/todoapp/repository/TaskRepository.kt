package com.ankit.todoapp.repository

import com.ankit.todoapp.data.room_database.TaskDao
import com.ankit.todoapp.data.room_database.TaskItem
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class TaskRepository @Inject constructor(private val dao: TaskDao) {

    fun getAllTasks(): Flow<List<TaskItem>> {
        return dao.getAllTasks()
    }

    suspend fun insert(task: TaskItem) = dao.insert(task)

    suspend fun update(task: TaskItem) = dao.update(task)

    suspend fun delete(task: TaskItem) = dao.delete(task)
}