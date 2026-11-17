package com.sebastianalvaradoz.laboratorio13_animaciones.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio1Visibilidad
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio2Color
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio3TamanoPosicion
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio4Contenido
import com.sebastianalvaradoz.laboratorio13_animaciones.juego.JuegoAtrapaLaEstrella

data class Ejercicio(
    val etiqueta: String,        // "Ejercicio 1", "Juego", ...
    val nombre: String,          // Nombre corto que se muestra en la tarjeta
    val descripcion: String,
    val icono: String,           // Emoji de la tarjeta
    val colores: List<Color>,    // Degradado de la tarjeta
    val contenido: @Composable () -> Unit,
    // true = se muestra con una tarjeta especial en el menú (para el juego)
    val destacado: Boolean = false
) {
    // Título que se muestra en la barra superior del ejercicio
    val titulo: String get() = "$etiqueta: $nombre"
}

// Cada ejercicio nuevo se agrega a esta lista
val listaEjercicios: List<Ejercicio> = listOf(
    Ejercicio(
        etiqueta = "Ejercicio 1",
        nombre = "AnimatedVisibility",
        descripcion = "Mostrar y ocultar un cuadro con fadeIn y fadeOut",
        icono = "👻",
        colores = listOf(Color(0xFF6A11CB), Color(0xFFB24BF3)),
        contenido = { Ejercicio1Visibilidad() }
    ),
    Ejercicio(
        etiqueta = "Ejercicio 2",
        nombre = "animateColorAsState",
        descripcion = "Transición de color azul ↔ verde con tween y spring",
        icono = "🎨",
        colores = listOf(Color(0xFF1E88E5), Color(0xFF43A047)),
        contenido = { Ejercicio2Color() }
    ),
    Ejercicio(
        etiqueta = "Ejercicio 3",
        nombre = "Tamaño y posición",
        descripcion = "animateDpAsState, Modifier.offset y orden de modificadores",
        icono = "🚀",
        colores = listOf(Color(0xFFF4511E), Color(0xFFFFA726)),
        contenido = { Ejercicio3TamanoPosicion() }
    ),
    Ejercicio(
        etiqueta = "Ejercicio 4",
        nombre = "AnimatedContent",
        descripcion = "Transición entre estados Cargando, Contenido y Error",
        icono = "🔄",
        colores = listOf(Color(0xFF00897B), Color(0xFF26C6DA)),
        contenido = { Ejercicio4Contenido() }
    ),
    Ejercicio(
        etiqueta = "Juego",
        nombre = "Atrapa la Estrella",
        descripcion = "Ejercicio final · combina todas las animaciones",
        icono = "⭐",
        colores = listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF7B1FA2)),
        contenido = { JuegoAtrapaLaEstrella() },
        destacado = true
    ),
)
