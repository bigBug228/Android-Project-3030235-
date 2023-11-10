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
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButtonDefaults
import android.content.Intent
import androidx.compose.ui.platform.LocalContext

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
                CheckboxQuestionScreen(navController = navController)
            }
            composable("question/3"){
                RadioQuestionScreen(navController = navController)
            }
            composable("question/4"){
                ResultScreen(navController = navController)
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
@Composable
fun CheckboxQuestionScreen(navController: NavHostController) {
    val options = listOf("Red", "Orange", "Yellow", "Green", "Blue", "Violet")
    val selectedOptions = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Select the colors of the rainbow.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextColor

        )
        Spacer(modifier = Modifier.height(16.dp))

        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = { checked ->
                        if (checked) {
                            selectedOptions.add(option)
                        } else {
                            selectedOptions.remove(option)
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = BlueGreenGradientEnd,
                        uncheckedColor = BlueGreenGradientEnd.copy(alpha = 0.6f), // Optional: Adjust alpha for unchecked state
                        checkmarkColor = LightTextColor // Optional: Adjust color of the checkmark if necessary
                    ),
                )
                Text(text = option,color = LightTextColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if(selectedOptions.size==6){
                        score+=1
                    }
                    navController.navigate("question/3")
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
@Composable
fun RadioQuestionScreen(navController: NavHostController) {
    val options = listOf("Mars", "Venus", "Jupiter", "Saturn")
    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Which planet is known as the Red Planet?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { selectedOption = option },
                    modifier = Modifier.padding(8.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BlueGreenGradientEnd,
                        unselectedColor = BlueGreenGradientEnd.copy(alpha = 0.6f) // Optional: Adjust alpha for unselected state
                    ),
                )
                Text(text = option,color = LightTextColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if(selectedOption.equals("Mars",ignoreCase = true)){
                        score+=1
                    }
                    navController.navigate("question/4")
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueGreenGradientEnd // Use containerColor for the button background
                )
            ) {
                Text("Next")
            }
        }
        // You can add logic to handle the end of questions here
    }
}
@Composable
fun ResultScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Style the Text for the result
        Text(
            "Final Result is $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextColor,
            modifier = Modifier.align(Alignment.CenterHorizontally) // Center horizontally in the Column
        )

        // Modifier to ensure the KonfettiView fills the remaining space
        KonfettiView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            parties = listOf(
                Party(
                    speed = 4f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    position = Position.Relative(0.5, 0.5), // Centered
                    emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
                )
            )
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                score =0
                val intent = Intent(context, Menu::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueGreenGradientEnd // Use containerColor for the button background
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Finish", color = LightTextColor)
        }
    }
}
