package com.example.testerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

var score =0
val BlueGreenGradientStart = Color(0xFF0081A7)
val BlueGreenGradientEnd = Color(0xFF00BFA5)
val AccentColor = Color(0xFFFF6B6B)
val NeutralColor = Color(0xFFF0F0F0)
val DarkTextColor = Color(0xFF002D62)

// Secondary color for text and button
val LightTextColor = Color(0xFFFFFFFF)
class Test : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var color by remember { mutableStateOf(intent.getStringExtra("background color") ?: "") }
            NavigationScreen(color)
        }
    }
}
@Composable
fun NavigationScreen(color:String) {
    val navController = rememberNavController()
    var backgroundColor by remember { mutableStateOf(Color.White) }
    if(color =="darkblue"){
        backgroundColor = Color(0xFF002D62)
    }else if (color =="blue"){
        backgroundColor = Color(0xFF0081A7)
    }
    Surface(modifier = Modifier.fillMaxSize(),color = backgroundColor) {
        NavHost(navController = navController, startDestination = "question/1"){
            composable("question/1"){
                StringQuestionScreen(navController = navController)
            }
            composable("question/2"){
                //CheckboxQuestionScreen(navController = navController)
            }
            composable("question/3"){
                //RadioQuestionScreen(navController = navController)
            }
            composable("question/4"){
                //ResultScreen(navController = navController)
            }

        }

    }
}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun StringQuestionScreen(navController: NavHostController) {
    var userAnswer by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "What is the capital of France?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextColor
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Your Answer",color = LightTextColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = BlueGreenGradientEnd, // Use containerColor for Material 3
                textColor = LightTextColor,
                cursorColor = LightTextColor,
                // You might also want to set other colors like focusedLabelColor, unfocusedLabelColor etc.
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if(userAnswer.equals("Paris",ignoreCase = true)){
                        score +=1
                    }
                    navController.navigate("question/2")
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueGreenGradientEnd // Use containerColor for the button background
                )
            ) {
                Text("Next")
            }
        }
    }
}
