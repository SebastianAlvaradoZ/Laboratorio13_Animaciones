package com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

// Los tres estados posibles de la pantalla
enum class EstadoCarga(val etiqueta: String) {
    Cargando("Cargando"),
    Contenido("Contenido"),
    Error("Error")
}

// Tiempos personalizados de la transición
private const val DURACION_ENTRADA_MS = 600
private const val RETRASO_ENTRADA_MS = 200
private const val DURACION_SALIDA_MS = 300

@Composable
fun Ejercicio4Contenido() {
    var estado by remember { mutableStateOf(EstadoCarga.Contenido) }
    // Cada vez que aumenta, se lanza una "carga" simulada
    var intentos by remember { mutableIntStateOf(0) }

    // Simula una petición: muestra Cargando 2 s y luego termina en Contenido o Error al azar
    LaunchedEffect(intentos) {
        if (intentos > 0) {
            estado = EstadoCarga.Cargando
            delay(2000)
            estado = if (Random.nextBoolean()) EstadoCarga.Contenido else EstadoCarga.Error
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Elige un estado:", style = MaterialTheme.typography.bodyLarge)

        // Botones para cambiar de estado manualmente
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EstadoCarga.entries.forEach { opcion ->
                val modificador = Modifier.weight(1f)
                val relleno = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                if (opcion == estado) {
                    Button(onClick = { estado = opcion }, modifier = modificador, contentPadding = relleno) {
                        Text(opcion.etiqueta)
                    }
                } else {
                    OutlinedButton(onClick = { estado = opcion }, modifier = modificador, contentPadding = relleno) {
                        Text(opcion.etiqueta)
                    }
                }
            }
        }

        Button(onClick = { intentos++ }) {
            Text("Simular carga (2 s)")
        }

        // AnimatedContent hace la transición entre un estado y otro en el mismo espacio
        AnimatedContent(
            targetState = estado,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = DURACION_ENTRADA_MS,
                        delayMillis = RETRASO_ENTRADA_MS
                    )
                ) togetherWith fadeOut(animationSpec = tween(durationMillis = DURACION_SALIDA_MS))
            },
            contentAlignment = Alignment.Center,
            label = "estadoCarga"
        ) { estadoActual ->
            when (estadoActual) {
                EstadoCarga.Cargando -> PantallaCargando()
                EstadoCarga.Contenido -> PantallaContenido()
                EstadoCarga.Error -> PantallaError(onReintentar = { intentos++ })
            }
        }

        Text(
            text = "Entrada: fadeIn $DURACION_ENTRADA_MS ms (retraso $RETRASO_ENTRADA_MS ms) · " +
                "Salida: fadeOut $DURACION_SALIDA_MS ms",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PantallaCargando() {
    TarjetaEstado {
        CircularProgressIndicator(modifier = Modifier.size(56.dp))
        Text("Cargando datos...", style = MaterialTheme.typography.titleMedium)
        Text("Por favor espera un momento", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun PantallaContenido() {
    TarjetaEstado(colorFondo = MaterialTheme.colorScheme.primaryContainer) {
        Text("✓", style = MaterialTheme.typography.displayMedium)
        Text("Datos cargados", style = MaterialTheme.typography.titleMedium)
        Text(
            "Jugador: Sebastian · Nivel 7 · 1250 puntos",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PantallaError(onReintentar: () -> Unit) {
    TarjetaEstado(colorFondo = MaterialTheme.colorScheme.errorContainer) {
        Text("✕", style = MaterialTheme.typography.displayMedium)
        Text("Error de conexión", style = MaterialTheme.typography.titleMedium)
        Text("No se pudieron cargar los datos", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}

@Composable
private fun TarjetaEstado(
    colorFondo: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 240.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 240.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            contenido()
        }
    }
}
