package com.sebastianalvaradoz.laboratorio13_animaciones.navegacion

import androidx.compose.runtime.Composable
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio1Visibilidad
import com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios.Ejercicio2Color

data class Ejercicio(
    val titulo: String,
    val descripcion: String,
    val contenido: @Composable () -> Unit
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
)
