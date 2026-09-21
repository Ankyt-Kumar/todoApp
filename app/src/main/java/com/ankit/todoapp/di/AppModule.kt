package com.ankit.todoapp.di

import android.content.Context
import androidx.room.Room
import com.ankit.todoapp.data.room_database.TaskDao
import com.ankit.todoapp.data.room_database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase {

        return Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            "tasks_database"
        ).build()

    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }
}