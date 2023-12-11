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
import android.database.sqlite.SQLiteDatabase
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.RectangleShape
import android.hardware.SensorManager
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorEvent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import android.content.ContentValues
import android.database.Cursor
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.Divider
import java.lang.StringBuilder


private var data = mutableStateOf("Empty database")
private lateinit var databaseManager: DatabaseManager
private lateinit var database : SQLiteDatabase
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this,"withScoreMenu.db",null,1)
        database = databaseManager.writableDatabase
        setContent {
            val context = LocalContext.current
            val sensorManager : SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val deviceSensors : List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
            if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)!=null) {

                val temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
                sensorManager.registerListener(
                    sensorEventListener,
                    temperature,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
                //Text("temperature " + temp.value.toString())
            }
            val finalDataBase = retrieveData()

            //call MyApp function
            MyApp(temp.value.toString(),finalDataBase.toString())
        }
    }
    var temp = mutableStateOf(0f)

    private val sensorEventListener : SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if(event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                temp.value = event.values[0]
            }
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // implement this if needed
        }
    }
private fun retrieveData(): List<Pair<String, Int>> {
    val dataList = mutableListOf<Pair<String, Int>>()
    val columns = arrayOf("Name", "Score")
    val cursor = database.query("Test", columns, null, null, null, null, null)

    while (cursor.moveToNext()) {
        val name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
        val score = cursor.getInt(cursor.getColumnIndexOrThrow("Score"))
        dataList.add(Pair(name, score))
    }
    cursor.close()

    return dataList
}
    private fun addData() {
        val row: ContentValues = ContentValues().apply {
            put("Name", "Tom")
            // Score will be set to its default value of 0
        }
        val row2: ContentValues = ContentValues().apply {
            put("Name", "Ann")
            // Score will be set to its default value of 0
        }
        database.insert("Test", null, row)
        database.insert("Test", null, row2)
    }
    private fun updateName(id: Int, newName: String) {
        val contentValues = ContentValues().apply {
            put("Name", newName)
        }
        val whereClause = "Id = ?"
        val whereArgs = arrayOf(id.toString())
        database.update("Test", contentValues, whereClause, whereArgs)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(temperature : String,stringDatabase: String) {
    //create name variable to pass it to Menu activity
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        //call demo screen function to set image for the background
        DemoScreen(temperature)

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
//        for(sensor in deviceSensors)
//        {
            //Text(sensor.name)
//        }
        //text field to enter name
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
        Button(
            onClick = {
                //use intent to move to the Menu activity and pass name value to it
                val intent = Intent(context, Results::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueGreenGradientEnd // Use containerColor for the button background
            )
        ) {
            Text("ShowAllResults", color = LightTextColor)
            //Text(stringDatabase, color = LightTextColor)

        }
        Spacer(modifier = Modifier.weight(1f))
        // Go to the menu activity button
        Button(
            onClick = {
                //use intent to move to the Menu activity and pass name value to it
                if (name.isNotEmpty()) {
                    val intent = Intent(context, Menu::class.java)
                    //add new name to database if it does not exists there
                    addDataIfNotExists(name)
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
            Text("Go to Menu", color = LightTextColor)
            //Text(stringDatabase, color = LightTextColor)
        }

    }
}
// function which adds new name to the database if it does not exists there
private fun addDataIfNotExists(name: String) {
    // check if name exists in database
    if (!isNamePresent(name)) {
        // app adds name when it did not find it in database
        val contentValues = ContentValues().apply {
            put("Name", name)
            // score will be default
        }
        database.insert("Test", null, contentValues)
    } else {
        // name exists
    }
}

//function which checks if name already exists in the database
private fun isNamePresent(name: String): Boolean {
    //define column which will be retrieved from database
    val columns = arrayOf("Name")
    //specify search criteria
    val selection = "Name = ?"
    //replace placeholder in selection
    val selectionArgs = arrayOf(name)
    //query database to find records matching criteria
    val cursor = database.query("Test", columns, selection, selectionArgs, null, null, null)
    // indicate presence of data by cursor movement
    val exists = cursor.moveToFirst()
    //release database resources
    cursor.close()

    return exists
}
@Composable
fun DemoScreen(temperature: String) {
    val context = LocalContext.current
    // convert temperature to the float value
    val tempValue = temperature.toFloatOrNull() ?: 0f

    // this code normalizes temperature to range [0,1]
    val normalizedTemp = (tempValue + 40) / 80 // [-40 to 40] degrees range

    // implementing interpolation between blue and yellow according to the temperature
    val interpolatedColor = lerpColor(Color(0xFF00BFFF), Color(0xFFFFD700), normalizedTemp)

    // implement animation of the transition between colours
    val animatedColor by animateColorAsState(targetValue = interpolatedColor)
    // convert color to integer
    val colorInt = android.graphics.Color.argb(
        (animatedColor.alpha * 255).toInt(),
        (animatedColor.red * 255).toInt(),
        (animatedColor.green * 255).toInt(),
        (animatedColor.blue * 255).toInt()
    )

    // save color in shared preferences
    val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putInt("BackgroundColor", colorInt)
        apply()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.minima),
            contentDescription = "backgroundImage",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
            colorFilter = ColorFilter.tint(animatedColor, BlendMode.Overlay) // overlay blend mode
        )
    }
}

// create function in order to interpolate between colors
fun lerpColor(colorStart: Color, colorEnd: Color, fraction: Float): Color {
    return Color(
        red = lerp(colorStart.red, colorEnd.red, fraction),
        green = lerp(colorStart.green, colorEnd.green, fraction),
        blue = lerp(colorStart.blue, colorEnd.blue, fraction),
        alpha = 1f //  alpha as equal to 1 to keep image visible
    )
}

// interpolate between two values using this function
fun lerp(startValue: Float, endValue: Float, fraction: Float): Float {
    // clamping the fraction between 0f and 1f
    val clampedFraction = fraction.coerceIn(0f, 1f)
    return startValue + (endValue - startValue) * clampedFraction
}