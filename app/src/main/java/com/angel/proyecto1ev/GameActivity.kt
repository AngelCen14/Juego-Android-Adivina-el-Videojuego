package com.angel.proyecto1ev

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class GameActivity : AppCompatActivity() {
    // Componentes del Layout
    private lateinit var listaBotones: List<Button>
    private lateinit var botonPulsado: Button
    private lateinit var botonSiguiente: Button
    private lateinit var botonInfoJuego: Button
    private lateinit var textViewPuntuacion: TextView
    private lateinit var imageViewVideojuego: ImageView

    // Variables para controlar el juego
    private lateinit var mapVideojuegos: Map<String, Pair<Int, String>>
    private lateinit var videojuegosDisponibles: MutableSet<String>
    private lateinit var videojuegoActual: String
    private lateinit var respuestaCorrecta: String
    private lateinit var nombreJugador: String
    private var estadoBotones = true
    private var imagenActual: Int = 0
    private var puntuacion: Int = 0

    // Colores de los botones
    private val colorError = Color.parseColor("#FF0000")
    private val colorAcierto = Color.parseColor("#00FF00")
    private val colorEstandar = Color.parseColor("#FF3D00")

    // Claves para guardar y recuperar datos
    companion object {
        const val KEY_PUNTUACION = "PUNTUACION"
        const val KEY_JUGADOR = "JUGADOR"
        const val KEY_JUEGO_ACTUAL = "JUEGO_ACTUAL"
        const val KEY_JUEGOS_DISPONIBLES = "JUEGOS_DISPONIBLES"
        const val KEY_IMAGEN_ACTUAL = "IMAGEN"
        const val KEY_TEXTO_BOTONES = "TEXTO_BOTONES"
        const val KEY_ESTADO_BOTONES = "ESTADO_BOTONES"
        const val KEY_PREFERENCIAS = "SHARED_PREFERENCES"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Inicializar todos los componentes
        imageViewVideojuego = findViewById(R.id.imageViewJuego)
        botonSiguiente = findViewById(R.id.botonSiguiente)
        botonInfoJuego = findViewById(R.id.botonInfo)
        textViewPuntuacion = findViewById(R.id.textViewPuntuacion)

        // Cargar datos del MainActivity
        if (intent != null && intent.hasExtra("NOMBRE_JUGADOR")) {
            nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR").toString()
        }

        // Cargar el mapa de los juegos y la lista de botones
        cargarVideojuegos()
        cargarBotones()

        // Iniciar el juego
        cargarNuevaPregunta()
        asignarOpcionesAleatorias()
        actualizarInterfaz()
    }

    /* FUNCIONES JUEGO */
    private fun cargarNuevaPregunta() {
        // Seleccionar un videojuego aleatorio de los disponibles
        videojuegoActual = videojuegosDisponibles.random()
        videojuegosDisponibles.remove(videojuegoActual)

        val infoVideojuego = mapVideojuegos[videojuegoActual]
        if (infoVideojuego != null) {
            imagenActual = infoVideojuego.first
            respuestaCorrecta = videojuegoActual
        }
    }

    private fun asignarOpcionesAleatorias() {
        // Asignar la opcion correcta a un boton aleatorio
        val botonCorrecto = listaBotones.random()
        asignarBoton(botonCorrecto, respuestaCorrecta)

        // Obtener una lista con los botones incorrectos
        val botonesIncorrectos = listaBotones.filter {
            it != botonCorrecto
        }

        // Generar una lista con los nombres de todos los juegos incorrectos
        val videojuegosIncorrectos = ArrayList(mapVideojuegos.keys - respuestaCorrecta)

        // Poner videojuegos aleatoriamente en los botones incorrectos
        for (boton in botonesIncorrectos) {
            val nombreAleatorio = videojuegosIncorrectos.random()
            asignarBoton(boton, nombreAleatorio)
            videojuegosIncorrectos.remove(nombreAleatorio)
        }

    }

    private fun verificarRespuesta(opcionSeleccionada: String) {
        if (opcionSeleccionada == respuestaCorrecta) {
            // Respuesta correcta
            botonPulsado.setBackgroundColor(colorAcierto)
            puntuacion += 2
        } else {
            // Respuesta incorrecta
            botonPulsado.setBackgroundColor(colorError)
            puntuacion--
        }

        textViewPuntuacion.text = "$nombreJugador: $puntuacion puntos"
        setEstadoBotones(false)
    }

    /* FUNCIONES PARA MODIFICAR LA INTERFAZ */
    private fun actualizarInterfaz() {
        imageViewVideojuego.setImageResource(imagenActual)
        setColorBotones(colorEstandar)
        setEstadoBotones(true)
        textViewPuntuacion.text = "$nombreJugador: $puntuacion puntos"
    }

    private fun asignarBoton(boton: Button, texto: String) {
        boton.text = texto
        boton.setOnClickListener {
            botonPulsado = boton
            verificarRespuesta(texto)
        }
    }

    private fun setColorBotones(color: Int) {
        for (boton in listaBotones) {
            boton.setBackgroundColor(color)
        }
    }

    private fun setEstadoBotones(estado: Boolean) {
        botonSiguiente.isEnabled = !estado
        botonInfoJuego.isEnabled = !estado
        for (boton in listaBotones) {
            boton.isEnabled = estado
        }
        estadoBotones = estado
    }

    /* FUNCIONES BOTONES */
    fun botonSiguienteOnClick(view: View) {
        if (videojuegosDisponibles.isEmpty()) {
            // Si no hay más juegos disponibles cerrar la activity
            Toast.makeText(this, "Puntuacion añadida a la tabla", Toast.LENGTH_SHORT).show()
            guardarPuntuacion()
            finish()
        } else {
            cargarNuevaPregunta()
            asignarOpcionesAleatorias()
            actualizarInterfaz()
        }
    }

    fun botonInfoOnClick(view: View) {
        val url = mapVideojuegos[respuestaCorrecta]?.second
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    /* FUNCIONES PARA CARGAR Y GUARDAR DATOS*/
    private fun cargarVideojuegos() {
        mapVideojuegos = mapOf(
            "Age of Empires IV" to Pair(R.drawable.age_of_empires_4, "https://www.ageofempires.com/games/age-of-empires-iv/"),
            "Call of Dutty: Black Ops 2" to Pair(R.drawable.cod_black_ops_2, "https://www.callofduty.com/es/blackops2"),
            "Counter Strike 2" to Pair(R.drawable.counter_strike_2, "https://www.counter-strike.net/home"),
            "Cyberpunk 2077" to Pair(R.drawable.cyberpunk_2077, "https://www.cyberpunk.net/es/es/"),
            "Dark Souls" to Pair(R.drawable.dark_souls, "https://es.bandainamcoent.eu/dark-souls/dark-souls"),
            "DBZ: Budokai Tenkaichi 3" to Pair(R.drawable.dbz_budokai_tenkaichi_3, "https://dragonball.fandom.com/es/wiki/Dragon_Ball_Z:_Budokai_Tenkaichi_3"),
            "DBZ: Kakarot" to Pair(R.drawable.dbz_kakarot, "https://es.bandainamcoent.eu/dragon-ball/dragon-ball-z-kakarot"),
            "Fallout 4" to Pair(R.drawable.fallout4, "https://fallout.bethesda.net/en/games/fallout-4"),
            "Fortnite" to Pair(R.drawable.fortnite, "https://www.fortnite.com/?lang=es-ES"),
            "God of War" to Pair(R.drawable.god_of_war, "https://www.playstation.com/es-es/god-of-war/"),
            "GTA V" to Pair(R.drawable.gta_5, "https://www.rockstargames.com/gta-v"),
            "Half-Life 2" to Pair(R.drawable.half_life_2, "https://www.half-life.com/es/halflife2"),
            "Halo 3" to Pair(R.drawable.halo_3, "https://www.halopedia.org/Halo_3"),
            "Halo Reach" to Pair(R.drawable.halo_reach, "https://www.halopedia.org/Halo:_Reach"),
            "Left 4 Dead 2" to Pair(R.drawable.left_4_dead_2, "https://www.l4d.com/game.html"),
            "Minecraft" to Pair(R.drawable.minecraft, "https://www.minecraft.net/es-es"),
            "Portal 2" to Pair(R.drawable.portal_2, "https://www.thinkwithportals.com"),
            "Red Dead Redemption" to Pair(R.drawable.red_dead_redemption, "https://www.rockstargames.com/reddeadredemption"),
            "Red Dead Redemption 2" to Pair(R.drawable.red_dead_redemption_2, "https://www.rockstargames.com/reddeadredemption2/"),
            "Sekiro" to Pair(R.drawable.sekiro, "https://www.sekirothegame.com/es/"),
            "Skyrim" to Pair(R.drawable.skyrim, "https://elderscrolls.bethesda.net/en/skyrim10"),
            "The Witcher 3" to Pair(R.drawable.the_witcher_3, "https://www.thewitcher.com/es/es/witcher3"),
            "World of Warcraft" to Pair(R.drawable.world_of_warcraft, "https://worldofwarcraft.blizzard.com/es-es/"),
            "Zelda: Tears of the Kingdom" to Pair(R.drawable.zelda_totk, "https://zelda.nintendo.com/tears-of-the-kingdom/")
        )

        videojuegosDisponibles = mapVideojuegos.keys.toMutableSet()
    }

    private fun cargarBotones() {
        listaBotones = listOf(
            findViewById(R.id.botonOpcion1),
            findViewById(R.id.botonOpcion2),
            findViewById(R.id.botonOpcion3),
            findViewById(R.id.botonOpcion4)
        )
    }

    private fun guardarPuntuacion() {
        // Obtener las puntuaciones guardadas desde las SharedPreferences
        val sharedPreferences = getSharedPreferences(KEY_PREFERENCIAS, MODE_PRIVATE)
        val puntuacionesGuardadas = sharedPreferences.getString(KEY_PUNTUACION, "")

        // Concatenar la nueva puntuacion
        val nuevaPuntuacion = "$nombreJugador: $puntuacion puntos"
        val listaActualizada = "$puntuacionesGuardadas\n$nuevaPuntuacion"

        // Guardar la puntuacion en las SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PUNTUACION, listaActualizada)
        editor.commit()
    }

    /* GUARDAR Y RESTAURAR ESTADO DE LA ACTIVIDAD EN CASO DE GIRAR EL MOVIL */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_JUGADOR, nombreJugador)
        outState.putInt(KEY_PUNTUACION, puntuacion)
        outState.putString(KEY_JUEGO_ACTUAL, videojuegoActual)
        outState.putStringArrayList(KEY_JUEGOS_DISPONIBLES, ArrayList(videojuegosDisponibles))
        outState.putInt(KEY_IMAGEN_ACTUAL, imagenActual)

        // Guardar el texto de los botones y su estado
        val textosBotones = listaBotones.map { it.text.toString() }
        outState.putStringArrayList(KEY_TEXTO_BOTONES, ArrayList(textosBotones))
        outState.putBoolean(KEY_ESTADO_BOTONES, estadoBotones)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nombreJugador = savedInstanceState.getString(KEY_JUGADOR).toString()
        puntuacion = savedInstanceState.getInt(KEY_PUNTUACION)
        videojuegoActual = savedInstanceState.getString(KEY_JUEGO_ACTUAL).toString()
        videojuegosDisponibles = HashSet(savedInstanceState.getStringArrayList(KEY_JUEGOS_DISPONIBLES))
        imagenActual = savedInstanceState.getInt(KEY_IMAGEN_ACTUAL)
        respuestaCorrecta = videojuegoActual

        // Cargar el texto de los botones y su estado
        val textosBotones = savedInstanceState.getStringArrayList(KEY_TEXTO_BOTONES)
        if (textosBotones != null) {
            for (i in 0 until textosBotones.size) {
                asignarBoton(listaBotones[i], textosBotones[i])
            }
        }

        actualizarInterfaz()
        setEstadoBotones(savedInstanceState.getBoolean(KEY_ESTADO_BOTONES))
    }
}