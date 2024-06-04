package com.angel.proyecto1ev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val botonVolver: Button = findViewById(R.id.botonVolver)
        botonVolver.setOnClickListener {
            finish()
        }
    }
}