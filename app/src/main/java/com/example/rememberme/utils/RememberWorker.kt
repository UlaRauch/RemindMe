package com.example.rememberme.utils

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.work.*
import com.example.rememberme.models.Reminder
import java.util.concurrent.TimeUnit

/**
 * code by https://dev.to/blazebrain/building-a-reminder-app-with-local-notifications-using-workmanager-api-385f
 */
class RememberWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        NotificationHelper(context = context).createNotification(
            /**
             * begin insertion by Ula Rauch
             * adaption to use autogenerated reminder id as notification id
             */
            id = inputData.getLong("id", 1).toInt(),
            /**
             * end insertion
             */
            title = inputData.getString("title").toString(),
            message = inputData.getString("message").toString())

        return Result.success()
    }
}