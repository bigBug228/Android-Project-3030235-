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

class Menu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                Divider()
            }
            item {

                // Settings section
                Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = LightTextColor)
            }
            item {
                //text field to edit name
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Edit Your Name",color = LightTextColor) },
                    modifier = Modifier.padding(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = BlueGreenGradientEnd,
                        textColor = LightTextColor,
                        cursorColor = LightTextColor,
                    )
                )
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

                // Start button
                Button(
                    onClick = {
                        if (backgroundColor == Color(0xFF002D62)) {
                            stringColor = "darkblue"
                        } else if (backgroundColor == Color(backgroundColorInt)) {
                            stringColor = "blue"
                        }
                        val newIntent = Intent(context, Test::class.java)
                        //pass string color to Test activity
                        newIntent.putExtra("background color", stringColor)
                        newIntent.putExtra("User name", name)
                        context.startActivity(newIntent)
                    },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueGreenGradientEnd
                    )
                ) {
                    Text("Start Test")
                }
            }
        }
    }
}

