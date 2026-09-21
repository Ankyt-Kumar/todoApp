package com.ankit.todoapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ankit.todoapp.data.room_database.TaskItem
import com.ankit.todoapp.viewmodel.TaskViewModel

@Composable
fun ToDoListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())

    var taskToEdit by remember { mutableStateOf<TaskItem?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    ToDoListContent(
        tasks = tasks,
        onAddTaskClick = {
            taskToEdit = null
            showEditDialog = true
        },
        onEditClick = { task ->
            taskToEdit = task
            showEditDialog = true
        },
        onDeleteClick = { task ->
            viewModel.deleteTask(task)
        },
        onCheckChange = { task, checked ->
            viewModel.updateTask(task.copy(isDone = checked))
        }
    )

    if (showEditDialog) {
        TaskEditorDialogue(
            task = taskToEdit,
            onSave = { taskName ->
                if (taskToEdit == null) {
                    viewModel.addTask(TaskItem(taskName = taskName))
                } else {
                    viewModel.updateTask(taskToEdit!!.copy(taskName = taskName))
                }
                showEditDialog = false
                taskToEdit = null
            },
            onCancel = {
                showEditDialog = false
                taskToEdit = null
            }
        )
    }
}

@Composable
fun ToDoListContent(
    tasks: List<TaskItem>,
    onAddTaskClick: () -> Unit,
    onEditClick: (TaskItem) -> Unit,
    onDeleteClick: (TaskItem) -> Unit,
    onCheckChange: (TaskItem, Boolean) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskClick,
                shape = RoundedCornerShape(20.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
                Text(
                    text = "Add Task",
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "My Tasks",
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${tasks.filter { !it.isDone }.size} Tasks",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (tasks.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Tasks",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else{
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        items = tasks,
                        key = {it.id}
                    ) { task ->
                        ToDoItem(
                            item = task,
                            onEditClick = { onEditClick(task) },
                            onDeleteClick = { onDeleteClick(task) },
                            onCheckChange = { checked -> onCheckChange(task, checked) }
                        )
                    }
                }
            }

        }
    }
}