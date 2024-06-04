package com.angel.proyecto1ev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TablaPuntuacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabla_puntuaciones)

        // Inicializar los componentes
        val textView = findViewById<TextView>(R.id.textViewPuntuaciones)
        val botonBorrar = findViewById<Button>(R.id.botonBorrar)

        // Obtener las puntaciones desde las SharedPreferences y mostrarlas en el TextView
        val sharedPreferences = getSharedPreferences(GameActivity.KEY_PREFERENCIAS, MODE_PRIVATE)
        val puntuaciones = sharedPreferences.getString(GameActivity.KEY_PUNTUACION, "")
        textView.text = puntuaciones

        botonBorrar.setOnClickListener {
            // Vaciar las SharedPreferences
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.commit()
            textView.text = ""
        }
    }
}