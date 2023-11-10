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
    var name by remember { mutableStateOf(intent.getStringExtra("User name") ?: "") }
    var isChecked by remember { mutableStateOf(false) }
    var backgroundColor by remember { mutableStateOf(Color(0xFF0081A7)) }
    var stringColor by remember { mutableStateOf("") }
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
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
                    Text("Welcome, $name!", color = Color.White, fontSize = 24.sp)
                }

                Divider()
            }
            item {

                // Settings section
                Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = LightTextColor)
            }
            item {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Edit Your Name",color = LightTextColor) },
                    modifier = Modifier.padding(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = BlueGreenGradientEnd, // Use containerColor for Material 3
                        textColor = LightTextColor,
                        cursorColor = LightTextColor,
                        // You might also want to set other colors like focusedLabelColor, unfocusedLabelColor etc.
                    )
                )
            }
            item {
                Switch(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        if (it) {
                            backgroundColor = Color(0xFF002D62)
                        } else {
                            backgroundColor = Color(0xFF0081A7)
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
                            stringColor = "darkBlue"
                        } else if (backgroundColor == Color(0xFF0081A7)) {
                            stringColor = "blue"
                        }
                        val newIntent = Intent(context, Test::class.java)
                        newIntent.putExtra("background color", stringColor)
                        context.startActivity(newIntent)
                    },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueGreenGradientEnd // Use containerColor for the button background
                    )
                ) {
                    Text("Start Test")
                }
            }
        }
    }
}

