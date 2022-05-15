package com.example.rememberme.screens

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.rememberme.models.Reminder
//import com.example.rememberme.models.getReminders
import com.example.rememberme.navigation.RememberScreens
import com.example.rememberme.viewmodels.RememberViewModel
import com.example.rememberme.widgets.RememberRow

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: RememberViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "RememberMe") },
            )
        }
    ) {
        MainContent(
            navController = navController,
            reminders = viewModel.reminders.collect() //TODO: room video weiter anschaun bei ca. 54:00
        )
    }
}

@Composable
fun MainContent(
    navController: NavController,
    reminders: List<Reminder>
) {
    LazyColumn() {
        items(reminders) { reminder ->
            RememberRow(
                reminder = reminder,
            )
            { reminderID ->
                //navController.navigate("HomeScreen")
                navController.navigate(RememberScreens.DetailScreen.name + "/$reminderID")
                Log.d("Navigation", "Reminder clicked. ID: ${reminder.id}")
            }

            /*
            Card() {
                Column() {
                    Text(text = reminder.title)
                    Text(text = reminder.text)
                }
            }

             */
        }

    }
}