package com.example.testerapp
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context,name : String,
                      factory : SQLiteDatabase.CursorFactory?,version : Int)
    :SQLiteOpenHelper(context,name,factory,version)
{
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createCommand)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(dropCommand)
        p0?.execSQL(createCommand)
    }
    private val createCommand = "CREATE TABLE Test (Id INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT NOT NULL,Score INTEGER DEFAULT 0)"
    private val dropCommand = "DROP TABLE Test"

}