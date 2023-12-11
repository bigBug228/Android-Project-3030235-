package com.example.testerapp

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.testerapp.ui.theme.TesterAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import java.time.format.TextStyle
import androidx.compose.ui.unit.sp
private var data = mutableStateOf("Empty database")
private lateinit var databaseManager: DatabaseManager
private lateinit var database : SQLiteDatabase
class Results : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this,"withScoreMenu.db",null,1)
        database = databaseManager.readableDatabase
        fetchData()
        setContent {
            ResultsApp(data.value, intent)
        }
    }
    private fun fetchData() {
        // query Test table without any conditions in order to retrieve all rows
        val cursor = database.query("Test", null, null, null, null, null, null)
        //accumulate data in readable format
        val stringBuilder = StringBuilder()
        //iterate over rows
        while (cursor.moveToNext()) {
            //retrieve name column value of current row
            val name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
            //retrieve score column value of current row
            val score = cursor.getInt(cursor.getColumnIndexOrThrow("Score"))
            //append string builder with name and score values of current row
            stringBuilder.append("Name: $name, Score: $score\n")
        }
        //release database resources
        cursor.close()
        //update live data object
        data.value = stringBuilder.toString().ifEmpty { "Empty database" }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsApp(dbData: String, intent: Intent) {
    val LightTextColor = Color(0xFFFFFFFF)
    val BlueGreenGradientEnd = Color(0xFF00BFA5)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(text = dbData, color = LightTextColor, fontSize = 20.sp)
            }
            item {
                Button(
                    onClick = {
                        val backIntent = Intent(context, MainActivity::class.java)
                        context.startActivity(backIntent)

                    },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueGreenGradientEnd
                    )
                ) {
                    Text("GoBack", color = LightTextColor)
                }
            }
        }

    }

}

