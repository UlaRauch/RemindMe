package com.example.remindme.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.remindme.DB.RememberDB
import com.example.remindme.models.Reminder
import com.example.remindme.repositories.RememberRepository
import com.example.remindme.screens.AddScreen
import com.example.remindme.screens.DetailScreen
import com.example.remindme.screens.EditScreen
import com.example.remindme.screens.HomeScreen
import com.example.remindme.utils.RememberWorker
import com.example.remindme.viewmodels.*

@Composable
fun RememberNavigation(themeViewModel: ThemeViewModel) {
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)
    val db = RememberDB.getDatabase(context = context)
    val repository = RememberRepository(db.remindersDao())
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RememberScreens.HomeScreen.name
    )
    {
        composable(RememberScreens.HomeScreen.name) {
            HomeScreen(
                navController = navController,
                repository = repository,
                workManager = workManager,
                themeViewModel = themeViewModel
            )
        }

        composable(
            route = RememberScreens.DetailScreen.name + "/{reminderID}",
            arguments = listOf(navArgument("reminderID") {
                type = NavType.LongType
            })
        ) {
            //get argument from backstack and pass as argument to detailscreen
                backStackEntry ->
            DetailScreen(
                reminderID = backStackEntry.arguments?.getLong("reminderID")!!,
                navController = navController,
                repository = repository,
                workManager = workManager
            )
        }

        composable(RememberScreens.AddScreen.name) {
            AddScreen(
                navController = navController,
                repository = repository,
                workManager = workManager,
                context = context
            )
        }

        composable(
            route = RememberScreens.EditScreen.name + "/{reminderID}",
            arguments = listOf(navArgument("reminderID") {
                type = NavType.LongType
            })
        ) {
            //get argument from backstack and pass as argument to detailscreen
                backStackEntry ->
            EditScreen(
                reminderID = backStackEntry.arguments?.getLong("reminderID")!!,
                repository = repository,
                navController = navController,
                workManager = workManager,
                context = context
            )
        }

    }
}
