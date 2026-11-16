package com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val DURACION_MS = 800

@Composable
fun Ejercicio1Visibilidad() {
    // Estado que controla si los cuadros se muestran o no
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Pulsa el botón para alternar la visibilidad",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ocultar cuadro" else "Mostrar cuadro")
        }

        // Comparación lado a lado: cambio instantáneo vs. AnimatedVisibility
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Sin animación", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                    // Un simple if: el cuadro aparece y desaparece de golpe
                    if (visible) {
                        CuadroDeColor(color = Color(0xFF9E9E9E), texto = "if")
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("AnimatedVisibility", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))
                CuadroAnimado(visible = visible)
            }
        }

        Text(
            text = "Entrada: fadeIn · Salida: fadeOut · $DURACION_MS ms",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CuadroDeColor(color: Color, texto: String) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = texto, color = Color.White, style = MaterialTheme.typography.titleLarge)
    }
}

// Va en una función aparte para que AnimatedVisibility no quede dentro del Column.
// Si se escribe dentro del Column, Kotlin elige la versión ColumnScope.AnimatedVisibility
// y da error de compilación al estar anidada dentro de un Box.
@Composable
private fun CuadroAnimado(visible: Boolean) {
    Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = DURACION_MS)),
            exit = fadeOut(animationSpec = tween(durationMillis = DURACION_MS))
        ) {
            CuadroDeColor(color = Color(0xFF6750A4), texto = "fade")
        }
    }
}
