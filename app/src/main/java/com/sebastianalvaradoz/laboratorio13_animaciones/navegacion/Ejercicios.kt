package com.sebastianalvaradoz.laboratorio13_animaciones.navegacion

import androidx.compose.runtime.Composable

data class Ejercicio(
    val titulo: String,
    val descripcion: String,
    val contenido: @Composable () -> Unit
)

// Cada ejercicio nuevo se agrega a esta lista
val listaEjercicios: List<Ejercicio> = listOf(
)
