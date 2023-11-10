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
//create variable to count correct answers
var score =0
//create values for colors which are used for design
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
            //get string value from menu activity which defines background color in test activity
            var color by remember { mutableStateOf(intent.getStringExtra("background color") ?: "") }
            //call navigation screen function and pass color value to it
            NavigationScreen(color)
        }
    }
}
@Composable
fun NavigationScreen(color:String) {
    val navController = rememberNavController()
    //logic for choosing background color for test activity
    var backgroundColor by remember { mutableStateOf(Color.White) }
    if(color =="darkblue"){
        backgroundColor = Color(0xFF002D62)
    }else if (color =="blue"){
        backgroundColor = Color(0xFF0081A7)
    }
    Surface(modifier = Modifier.fillMaxSize(),color = backgroundColor) {
        //use navigation system to move between screens. Each screen is a different question
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
    // create variable to store user answer
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
            //user will use this text field to answer question
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Your Answer",color = LightTextColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = BlueGreenGradientEnd,
                textColor = LightTextColor,
                cursorColor = LightTextColor,

            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //button to move to the next question
            Button(
                onClick = {
                    //check user answer, if its correct, add 1 to score value
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
    //list of all options for the user
    val options = listOf("Red", "Orange", "Yellow", "Green", "Blue", "Violet")
    //create list of options which user selected
    val selectedOptions = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //text to specify task for the user
        Text(
            text = "Select the colors of the rainbow.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextColor

        )
        Spacer(modifier = Modifier.height(16.dp))
        //for each option from the list create checkbox with text
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    //if user presses checkbox, this answer will be added to selected options list
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
                        uncheckedColor = BlueGreenGradientEnd.copy(alpha = 0.6f),
                        checkmarkColor = LightTextColor
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
            //button to move to the next question
            Button(
                //if selected options list has a size of six, score value will be +1
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
    //list of all options
    val options = listOf("Mars", "Venus", "Jupiter", "Saturn")
    //option which user selected
    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //text which will specify the task for the user
        Text(
            text = "Which planet is known as the Red Planet?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextColor
        )
        Spacer(modifier = Modifier.height(16.dp))
        //for each option program creates radio button with text
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
                        unselectedColor = BlueGreenGradientEnd.copy(alpha = 0.6f)
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
            //button to move to result screen
            Button(
                onClick = {
                    //if selected option equals to Mars, score + 1
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
    }
}
@Composable
fun ResultScreen(navController: NavHostController) {
    //context for intent which will start menu activity
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Styl
        Text(
            "Final Result is $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextColor,
            modifier = Modifier.align(Alignment.CenterHorizontally) // Center horizontally in the Column
        )

        //design element which created konfetti effect at the center of result screen for a few seconds
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
        //button which sets score back to zero and brings user back to menu activity. Can start test again
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
