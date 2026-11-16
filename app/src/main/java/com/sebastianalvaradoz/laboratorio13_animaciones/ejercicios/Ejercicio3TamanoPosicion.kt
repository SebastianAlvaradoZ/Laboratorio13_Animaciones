package com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val TAMANO_PEQUENO = 70.dp
private val TAMANO_GRANDE = 130.dp
private val FORMA = RoundedCornerShape(16.dp)

@Composable
fun Ejercicio3TamanoPosicion() {
    var expandido by remember { mutableStateOf(false) }
    // Cambia el orden de los modificadores para observar la diferencia
    var offsetPrimero by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = { expandido = !expandido }) {
            Text(if (expandido) "Volver al inicio" else "Mover y agrandar")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = offsetPrimero, onCheckedChange = { offsetPrimero = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (offsetPrimero) "Orden: offset → size → background"
                else "Orden: size → background → offset",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Área de juego donde se mueve el cuadro
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(FORMA)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        ) {
            // Destinos: el cuadro termina en la esquina inferior derecha del área
            val destinoTamano = if (expandido) TAMANO_GRANDE else TAMANO_PEQUENO
            val destinoX = if (expandido) maxWidth - TAMANO_GRANDE else 0.dp
            val destinoY = if (expandido) maxHeight - TAMANO_GRANDE else 0.dp

            val resorte = spring<Dp>(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
            val tamano by animateDpAsState(destinoTamano, resorte, label = "tamano")
            val x by animateDpAsState(destinoX, resorte, label = "offsetX")
            val y by animateDpAsState(destinoY, resorte, label = "offsetY")

            val modificador = if (offsetPrimero) {
                // offset primero: se mueve TODO (fondo y contenido)
                Modifier
                    .offset(x = x, y = y)
                    .size(tamano)
                    .background(Color(0xFFFF7043), FORMA)
            } else {
                // offset al final: el fondo se queda en su sitio y solo se mueve el contenido
                Modifier
                    .size(tamano)
                    .background(Color(0xFFFF7043), FORMA)
                    .offset(x = x, y = y)
            }

            Box(modifier = modificador, contentAlignment = Alignment.Center) {
                Text("★", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
        }

        Text(
            text = "Tamaño: ${textoTamano(expandido)} · animateDpAsState + Modifier.offset (spring)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

private fun textoTamano(expandido: Boolean): String =
    if (expandido) "${TAMANO_GRANDE.value.toInt()} dp" else "${TAMANO_PEQUENO.value.toInt()} dp"
