package com.angel.proyecto1ev

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    // Componentes del layout
    private lateinit var editTextNombre: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes del layout
        editTextNombre = findViewById(R.id.editTextNombre)
    }

    // Funciones botones
    fun botonTutorialOnClick(view: View) {
        abrirActivity(TutorialActivity::class.java)
    }

    fun botonTablaPuntuacionesOnClick(view: View) {
        abrirActivity(TablaPuntuacionesActivity::class.java)
    }

    fun botonJugarOnClick(view: View) {
        if (comprobarNombre()) {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("NOMBRE_JUGADOR", editTextNombre.text.toString())
            startActivity(intent)
        } else {
            Toast.makeText(this, "Introduce tu nombre para jugar", Toast.LENGTH_LONG).show()
        }
    }

    // Funcion para abrir una actividad
    private fun abrirActivity(activity: Class<out Activity>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    private fun comprobarNombre(): Boolean {
        val nombre: String = editTextNombre.text.toString()
        return !nombre.isNullOrEmpty()
    }
}