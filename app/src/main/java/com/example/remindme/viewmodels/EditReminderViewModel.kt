package com.example.remindme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.remindme.models.Reminder
import com.example.remindme.repositories.ReminderRepository
import com.example.remindme.utils.WorkRequestUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditReminderViewModel (
    private val repository: ReminderRepository,
    private val workManager: WorkManager,
    reminderID: Long
) : ViewModel() {
    val reminder: LiveData<Reminder> = repository.filterReminder(id = reminderID)
    private var _updatedReminder: Reminder? = reminder.value

    fun initializeReminder(){
        _updatedReminder = reminder.value
    }

    fun updateReminder() {
        _updatedReminder?.let { reminder ->
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateReminder(reminder = reminder)//
                //Log.d("ViewModel", "reminder updated")
                workManager.cancelAllWorkByTag(reminder.id.toString())
                WorkRequestUtils.createWorkRequest(reminder, workManager)
                //Log.d("ViewModel", "workrequest for: Month: ${reminder.m}(calendar index), Day: ${reminder.d}:")
            }
        }
    }
    fun setText(text: String) {
        _updatedReminder?.text = text //wenn string übergeben wird wird geschaut ob eh nicht null
    }
    fun setDate(d: Int, m: Int, y: Int) {
        _updatedReminder?.d = d //wenn string übergeben wird wird geschaut ob eh nicht null
        _updatedReminder?.m = m
        _updatedReminder?.y = y
    }
    fun setTime(h:Int, min: Int) {
        _updatedReminder?.h = h //wenn string übergeben wird wird geschaut ob eh nicht null
        _updatedReminder?.min = min

    }
    fun setTitle(title: String) {
        _updatedReminder?.title = title //wenn string übergeben wird wird geschaut ob eh nicht null
    }

    fun setSurprise(isSurprise: Boolean) {
        _updatedReminder?.isSurprise = isSurprise
    }

}