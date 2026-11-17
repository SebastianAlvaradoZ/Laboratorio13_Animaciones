package com.sebastianalvaradoz.laboratorio13_animaciones.navegacion

import androidx.compose.runtime.Composable
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio1Visibilidad
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio2Color
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio3TamanoPosicion
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio4Contenido
import com.sebastianalvaradoz.laboratorio13_animaciones.juego.JuegoAtrapaLaEstrella

data class Ejercicio(
    val titulo: String,
    val descripcion: String,
    val contenido: @Composable () -> Unit,
    // true = se muestra con una tarjeta especial en el menú (para el juego)
    val destacado: Boolean = false
)

// Cada ejercicio nuevo se agrega a esta lista
val listaEjercicios: List<Ejercicio> = listOf(
    Ejercicio(
        titulo = "Ejercicio 1: AnimatedVisibility",
        descripcion = "Mostrar y ocultar un cuadro con fadeIn y fadeOut",
        contenido = { Ejercicio1Visibilidad() }
    ),
    Ejercicio(
        titulo = "Ejercicio 2: animateColorAsState",
        descripcion = "Transición de color azul ↔ verde con tween y spring",
        contenido = { Ejercicio2Color() }
    ),
    Ejercicio(
        titulo = "Ejercicio 3: Tamaño y posición",
        descripcion = "animateDpAsState y Modifier.offset, y el orden de modificadores",
        contenido = { Ejercicio3TamanoPosicion() }
    ),
    Ejercicio(
        titulo = "Ejercicio 4: AnimatedContent",
        descripcion = "Transición entre estados Cargando, Contenido y Error",
        contenido = { Ejercicio4Contenido() }
    ),
    Ejercicio(
        titulo = "Juego: Atrapa la Estrella",
        descripcion = "Ejercicio final · combina todas las animaciones",
        contenido = { JuegoAtrapaLaEstrella() },
        destacado = true
    ),
)
