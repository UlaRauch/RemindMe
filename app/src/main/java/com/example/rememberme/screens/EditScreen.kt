package com.example.rememberme.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.WorkManager
import com.example.rememberme.models.Reminder
import com.example.rememberme.repositories.RememberRepository
import com.example.rememberme.ui.theme.Green600
import com.example.rememberme.ui.theme.Purple600
import com.example.rememberme.viewmodels.EditRememberViewModel
import com.example.rememberme.viewmodels.EditRememberViewModelFactory
import java.util.*


@Composable
fun EditScreen(
    navController: NavController,
    repository: RememberRepository,
    workManager: WorkManager,
    reminderID: Long = 1,
    context: Context
) {
    val editViewModel: EditRememberViewModel = viewModel(
        factory = EditRememberViewModelFactory(
            repository = repository,
            workManager = workManager,
            reminderId = reminderID
        )
    )

    val reminder =
        editViewModel.reminder.observeAsState()  // observe the livedata as state for recomposition

    Log.d("editscreen", "you're in the editscreen")
    //val reminder = Reminder(title = "next", d = 1, m = 1, y = 2023, h = 22, min = 0, text = "")
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit ${reminder.value?.title}",
                       // color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Arrow Back")
                    }
                },
              //  backgroundColor = Purple600,
              //  contentColor = Color.White
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editViewModel.updateReminder()
                    navController.popBackStack()
                },
                //backgroundColor = Green600,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Add Button",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        content = {
            // only show card if reminder is not null
            reminder.value?.let { reminder ->
                EditReminderCard(
                    editViewModel = editViewModel,
                    context = context,
                    reminder = reminder
                )
            }
        })
}

@Composable
fun EditReminderCard(
    editViewModel: EditRememberViewModel,
    reminder: Reminder,
    context: Context,
) {
    editViewModel.initializeReminder()
    //vars text, title
    var text by remember { mutableStateOf(reminder.text) }
    var title by remember { mutableStateOf(reminder.title) }
    //vars for date
    var y: Int by remember { mutableStateOf(reminder.y) }
    var m: Int by remember { mutableStateOf(reminder.m) }
    var d: Int by remember { mutableStateOf(reminder.d) }

    //vals and vars for Time
    var hReminder: Int by remember { mutableStateOf(reminder.h) }
    var minReminder: Int by remember { mutableStateOf(reminder.min) }
    val calTime = Calendar.getInstance()
    val date by remember { mutableStateOf(calTime) } //TODO: muss das stateful sein?
    var isSurprise by remember { mutableStateOf(reminder.isSurprise) }


    // TODO make time stateful too!
    // Declaring and initializing a calendar
    val nowTime = Calendar.getInstance()

    // Value for storing time as a string
    val mTime = remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
        //.height(130.dp) //remove this to keep height flexible
        ,
        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            //Title
            OutlinedTextField(
                value = title,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "EditIcon"
                    )
                },
                onValueChange = { value ->
                    title = value    // update text value inside field
                    editViewModel.setTitle(value)
                },
                label = { Text(text = reminder.title) },
                placeholder = { Text(text = "Edit Title") },
                modifier = Modifier
                    .padding(20.dp, 30.dp)
                    .fillMaxWidth()
            )
            //Date

            val datePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    y = year
                    m =
                        month //months are represented as index https://developer.android.com/reference/java/util/Date.html#Date%28int,%20int,%20int,%20int,%20int,%20int%29
                    d = dayOfMonth
                    editViewModel.setDate(d = d, m = m, y = y)
                    editViewModel.setSurprise(false) // is not a surprise reminder
                    isSurprise = false
                }, y, m, d
            )


            Button(
                onClick = {
                    datePickerDialog.show()
                }, modifier = Modifier
                    .padding(20.dp, 5.dp)
                    .fillMaxWidth()
            ) {
                if (isSurprise == true) {
                    Text(text = "Change date")
                } else {
                    Text(
                        text = "${d.toString().padStart(2, '0')}.${
                            (m + 1).toString().padStart(2, '0')
                        }.$y"
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))

            //Time
            val mTimePickerDialog = TimePickerDialog(
                context,
                { _, mHour: Int, mMinute: Int ->
                    //calTime.set(mHour, mMinute)
                    hReminder = mHour
                    minReminder = mMinute
                    editViewModel.setTime(h = hReminder, min = minReminder)
                    editViewModel.setSurprise(false)
                    isSurprise = false
                }, hReminder, minReminder, false
            )


            Button(
                onClick = {
                    mTimePickerDialog.show()
                }, modifier = Modifier
                    .padding(20.dp, 5.dp)
                    .fillMaxWidth()
            ) {
                if (isSurprise) {
                    Text(text = "Change time")
                } else {
                    Text(
                        text = "${hReminder.toString().padStart(2, '0')}:${
                            (minReminder).toString().padStart(2, '0')
                        }"
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    modifier = Modifier.padding(30.dp, 5.dp),
                    selected = isSurprise,
                    onClick = {
                        if (!isSurprise) {
                            //Reset to current date/time is important to prevent cumulating dates when the radiobutton is clicked several times
                            date.set(
                                calTime.get(Calendar.YEAR),
                                calTime.get(Calendar.MONTH),
                                calTime.get(Calendar.DAY_OF_MONTH),
                                calTime.get(Calendar.HOUR_OF_DAY),
                                calTime.get(Calendar.MINUTE)
                            )
                            // set new random date + time within interval minDays - maxDays
                            val minDays = 2
                            val maxDays = 61
                            val randomDays = kotlin.random.Random.nextInt(minDays, maxDays)
                            date.add(Calendar.DAY_OF_MONTH, randomDays)
                            // a limitation to daytime notifications is possible by setting minHours and MaxHours to the desired interval
                            val minHours = 0
                            val maxHours = 24
                            val randomMinutes =
                                kotlin.random.Random.nextInt(minHours, ((maxHours * 60) - 1))
                            date.add(Calendar.MINUTE, randomMinutes)
                            Log.i("Add", "New surprise date: ${date.time}")

                            // set surprise time for reminder
                            y = date.get(Calendar.YEAR)
                            m = date.get(Calendar.MONTH)
                            d = date.get(Calendar.DAY_OF_MONTH)
                            hReminder = date.get(Calendar.HOUR_OF_DAY)
                            minReminder = date.get(Calendar.MINUTE)
                            editViewModel.setDate(d, m, y)
                            editViewModel.setTime(hReminder, minReminder)
                            editViewModel.setSurprise(true) // is a surprise reminder
                        } else {
                            editViewModel.setSurprise(false)
                        }
                        isSurprise = !isSurprise
                        Log.i(
                            "Add",
                            "isSurprise in VM is now: ${editViewModel.reminder.value?.isSurprise}"
                        )
                    }
                )
                Text(
                    text = "Surprise me!",
                    modifier = Modifier.fillMaxWidth()
                )
            }



            OutlinedTextField(
                //value = if (reminder != null) reminder!!.text else "", //schaut is reminder nicht null wenns da is dann wird der vom viewmodel angezeigt
                value = text,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "EditIcon"
                    )
                },
                // trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                // onValueChange = {value -> addViewModel.setText(value)}, // immer wenn ich text änder dann ändert sich das im reminderobjekt aktuallisiert
                onValueChange = { value ->
                    text = value    // update text value inside field
                    editViewModel.setText(value) // update value in viewmodel
                },
                label = { Text(text = reminder.text) },
                placeholder = { Text(text = "Edit Text") },
                modifier = Modifier
                    .padding(20.dp, 5.dp)
                    .fillMaxWidth()
            )
        }
    }
}

