package com.sebastianalvaradoz.laboratorio13_animaciones.navegacion

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun AppLaboratorio() {
    // null = estamos en el menú; un número = índice del ejercicio abierto
    var indiceSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    val ejercicioActual = indiceSeleccionado?.let { listaEjercicios.getOrNull(it) }

    if (ejercicioActual == null) {
        MenuPrincipal(
            ejercicios = listaEjercicios,
            onSeleccionar = { indiceSeleccionado = it }
        )
    } else {
        // El botón "atrás" del celular vuelve al menú en vez de cerrar la app
        BackHandler { indiceSeleccionado = null }
        PantallaEjercicio(
            titulo = ejercicioActual.titulo,
            onVolver = { indiceSeleccionado = null },
            contenido = ejercicioActual.contenido
        )
    }
}
