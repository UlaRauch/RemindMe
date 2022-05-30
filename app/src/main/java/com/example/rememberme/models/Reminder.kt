package com.example.rememberme.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var title: String,
    var d: Int,
    var m: Int,
    var y: Int,
    var h: Int = 0,
    var min: Int = 0,
    var text: String
    )
