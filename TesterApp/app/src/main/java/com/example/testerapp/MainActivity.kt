package com.example.testerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import android.widget.Toast
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.RectangleShape



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DemoScreen()

    }
    // Define the primary colors from the logo
    val BlueGreenGradientStart = Color(0xFF0081A7)
    val BlueGreenGradientEnd = Color(0xFF00BFA5)
    val AccentColor = Color(0xFFFF6B6B)
    val NeutralColor = Color(0xFFF0F0F0)
    val DarkTextColor = Color(0xFF002D62)

// Secondary color for text and button
    val LightTextColor = Color(0xFFFFFFFF)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            label = { Text("Enter Your Name",
                color = LightTextColor) },
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = BlueGreenGradientEnd, // Use containerColor for Material 3
                textColor = LightTextColor,
                cursorColor = LightTextColor,
                // You might also want to set other colors like focusedLabelColor, unfocusedLabelColor etc.
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (name.isNotEmpty()) {
                    val intent = Intent(context, Menu::class.java)
                    intent.putExtra("User name", name)
                    context.startActivity(intent)
                } else {
                    // Handle empty name input
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueGreenGradientEnd // Use containerColor for the button background
            )
        ) {
            Text("Go to Menu Activity", color = LightTextColor)
        }
    }
}
@Composable
fun DemoScreen(){
    Box(modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(id = R.drawable.minima),
            contentDescription = "backgroundImage",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}
