package com.pedroso.beelog

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewLocationActivity : AppCompatActivity() {

    private lateinit var editLocationLatView : EditText
    private lateinit var editLocationLonView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_location)

        editLocationLatView = findViewById<EditText>(R.id.textViewLatitude)
        editLocationLonView = findViewById<EditText>(R.id.textViewLongitude)

        val button = findViewById<Button>(R.id.button_save_location)
        button.setOnClickListener{
            val repplyIntent = Intent()
            if(TextUtils.isEmpty(editLocationLatView.text) or TextUtils.isEmpty(editLocationLonView.text)){
                setResult(Activity.RESULT_CANCELED, repplyIntent)
            } else {
                val lat = editLocationLatView.text.toString()
                val lon = editLocationLonView.text.toString()
                repplyIntent.putExtra(EXTRA_REPLY, lat +";" + lon)
                setResult(Activity.RESULT_OK, repplyIntent)
            }
            finish()
        }

    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.locationlistsql.REPLY"
    }
}