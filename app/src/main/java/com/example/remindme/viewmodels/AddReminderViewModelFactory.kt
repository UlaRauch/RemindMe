package com.example.remindme.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.remindme.repositories.ReminderRepository

class AddReminderViewModelFactory(
    private val repository: ReminderRepository,
    private val workManager: WorkManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddReminderViewModel::class.java)) {
            return AddReminderViewModel(
                repository = repository,
                workManager = workManager
            ) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}