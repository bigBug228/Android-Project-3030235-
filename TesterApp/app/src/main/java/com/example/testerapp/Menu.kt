package com.example.testerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues

private var data = mutableStateOf("Empty database")
private lateinit var databaseManager: DatabaseManager
private lateinit var database : SQLiteDatabase
class Menu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this,"withScoreMenu.db",null,1)
        database = databaseManager.writableDatabase

        setContent {
            MenuApp(intent)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuApp(intent: Intent) {

    // Define the primary colors from the logo
    val BlueGreenGradientStart = Color(0xFF0081A7)
    val BlueGreenGradientEnd = Color(0xFF00BFA5)
    val AccentColor = Color(0xFFFF6B6B)
    val NeutralColor = Color(0xFFF0F0F0)
    val DarkTextColor = Color(0xFF002D62)

    // Secondary color for text and button
    val LightTextColor = Color(0xFFFFFFFF)
    //receive name from main activity
    var name by remember { mutableStateOf(intent.getStringExtra("User name") ?: "") }
//    val currentName = retrieveName(name)
    var score by remember {mutableStateOf(retrieveScore(name))}
    var warningMessage by remember { mutableStateOf("") }
    //bool for switch button
    var isChecked by remember { mutableStateOf(false) }
    //background color of menu which can be changed
    //var backgroundColor by remember { mutableStateOf(Color(0xFF0081A7)) }
    //string value which program passes to Test activity in order to change its background
    var stringColor by remember { mutableStateOf("") }
    //context for intent
    val context = LocalContext.current
    //get colour from shared preferences
    val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val defaultColor = android.graphics.Color.WHITE // Default color if not set
    val backgroundColorInt = sharedPref.getInt("BackgroundColor", defaultColor)
    var backgroundColor by remember { mutableStateOf(Color(backgroundColorInt)) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        //make scrollable column for menu
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome message
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(16.dp)
                ) {
                    Text("Welcome, $name!", color = LightTextColor, fontSize = 24.sp)
                }
                //space between items
                Divider(color = LightTextColor)
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(16.dp)
                ) {
                    Text("Your current score is $score", color = LightTextColor, fontSize = 24.sp)
                }
                //space between items
                Divider(color = LightTextColor)
            }
            item {

                // Settings section
                Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = LightTextColor)
            }
            item {
                //text field to edit name
                TextField(
                    value = name,
                    onValueChange = { name = it
                        warningMessage = "" // Reset warning message

                        // Check if the name exists in the database
                        if (isNamePresent(name) && (name != (intent.getStringExtra("User name") ?: ""))) {
                            warningMessage = "Name already exists. Please choose a different name."
                        }
                        if(name.isBlank()){
                            warningMessage = "Name field is empty. Please choose a name."
                        }
                                    },
                    label = { Text("Edit Your Name",color = LightTextColor) },
                    modifier = Modifier.padding(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = BlueGreenGradientEnd,
                        textColor = LightTextColor,
                        cursorColor = LightTextColor,
                    )
                )
            }
            if (warningMessage.isNotEmpty()) {
                item {
                    Text(
                        text = warningMessage,
                        color = Color.Red, // or any warning color
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item {
                //switch for changing background color
                Switch(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        if (it) {
                            backgroundColor = Color(0xFF002D62)
                        } else {
                            backgroundColor = Color(backgroundColorInt)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = BlueGreenGradientEnd,
                        checkedTrackColor = BlueGreenGradientEnd.copy(alpha = 0.5f),
                        uncheckedThumbColor = DarkTextColor,
                        uncheckedTrackColor = DarkTextColor.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Button(
                    onClick = {
                        if (backgroundColor == Color(0xFF002D62)) {
                            stringColor = "darkblue"
                        } else if (backgroundColor == Color(backgroundColorInt)) {
                            stringColor = "blue"
                        }

                        val nameUpdateSuccess = updateNameIfNewNameNotExists(intent.getStringExtra("User name") ?: "", name)

                        if (nameUpdateSuccess) {
                            // name was successfully updated, proceed
                            val newIntent = Intent(context, Test::class.java)
                            newIntent.putExtra("background color", stringColor)
                            newIntent.putExtra("User name", name)
                            context.startActivity(newIntent)
                        } else {
                            // name update failed (name already exists or text field is empty), reset name to current name
                            val newIntent = Intent(context, Test::class.java)
                            newIntent.putExtra("background color", stringColor)
                            newIntent.putExtra("User name",intent.getStringExtra("User name") ?: "")
                            context.startActivity(newIntent)

                            // Possibly show a message to the user that the name is already in use
                        }
                    },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueGreenGradientEnd)
                ) {
                    Text("Start Test")
                }
            }
            item {
                //button which brings user back to main activity
                Button(
                    onClick = {
                        updateNameIfNewNameNotExists(intent.getStringExtra("User name") ?: "", name)
                        val backIntent = Intent(context, MainActivity::class.java)
                        context.startActivity(backIntent)
                    },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueGreenGradientEnd) // Red for delete
                ) {
                    Text("LogOut", color = Color.White)
                }
            }
            item {
                //button which brings user back to main activity and deletes his/her account from database
                Button(
                    onClick = {
                       deleteAccount(intent.getStringExtra("User name") ?: "")
                        val backIntent = Intent(context, MainActivity::class.java)
                        context.startActivity(backIntent)

                    },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Red for delete
                ) {
                    Text("Delete Account", color = Color.White)
                }
            }
        }
    }
}
//function which returns current score of current user
private fun retrieveScore(userName: String): Int? {
    //define column which will be retrieved from database
    val columns = arrayOf("Score")
    //specify search criteria
    val selection = "Name = ?"
    //replace placeholder in selection
    val selectionArgs = arrayOf(userName)
    val cursor = database.query("Test", columns, selection, selectionArgs, null, null, null)

    var score: Int? = null
    if (cursor.moveToFirst()) {
        //if user exists, get its score value from Score column
        score = cursor.getInt(cursor.getColumnIndexOrThrow("Score"))
    }
    cursor.close()
    //return integer score value
    return score
}

//function to update name of the user
private fun updateNameIfNewNameNotExists(currentName: String, newName: String): Boolean {
    // check if new name already exists and its not empty
    if (newName.isNotBlank() &&!isNamePresent(newName)) {
        // new name does not exist and its not empty. Now proceed with update
        val contentValues = ContentValues().apply {
            //insert new name into content values
            put("Name", newName)
        }
        //create where clause in order to specify which row to update
        val whereClause = "Name = ?"
        //specify current name as argument for where clause
        val whereArgs = arrayOf(currentName)
        //perform update operation
        val rowsUpdated = database.update("Test", contentValues, whereClause, whereArgs)
        // return true if any row was updated
        return rowsUpdated > 0
    } else {
        // return false because new name already exists
        return false
    }
}
//same function as from the main activity which checks if name already exists in database
private fun isNamePresent(name: String): Boolean {
    val columns = arrayOf("Name")
    val selection = "Name = ?"
    val selectionArgs = arrayOf(name)
    //query database to find records matching criteria
    val cursor = database.query("Test", columns, selection, selectionArgs, null, null, null)

    val exists = cursor.moveToFirst() // check if cursor is not empty
    cursor.close()

    return exists
}
//function which deletes user from database
private fun deleteAccount(userName: String): Boolean {
    //create where clause in order to specify which rows to delete
    val whereClause = "Name = ?"
    //specify user name to delete
    val whereArgs = arrayOf(userName)
    //exec delete operation
    val rowsDeleted = database.delete("Test", whereClause, whereArgs)

    return rowsDeleted > 0 // return true if any row was deleted
}
