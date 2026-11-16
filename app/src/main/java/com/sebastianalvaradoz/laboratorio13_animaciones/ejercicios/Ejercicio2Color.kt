package com.sebastianalvaradoz.laboratorio13_animaciones.ejercicios

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

private val Azul = Color(0xFF1E88E5)
private val Verde = Color(0xFF43A047)

@Composable
fun Ejercicio2Color() {
    var esAzul by remember { mutableStateOf(true) }
    var automatico by remember { mutableStateOf(false) }

    // Modo automático: alterna el color cada 1.5 s para ver la animación de forma continua
    LaunchedEffect(automatico) {
        while (automatico) {
            delay(1500)
            esAzul = !esAzul
        }
    }

    val colorDestino = if (esAzul) Azul else Verde

    // Misma transición de color con dos especificaciones de animación distintas
    val colorTween by animateColorAsState(
        targetValue = colorDestino,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "colorTween"
    )
    val colorSpring by animateColorAsState(
        targetValue = colorDestino,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "colorSpring"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Cambia el color entre azul y verde",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Button(onClick = { esAzul = !esAzul }, enabled = !automatico) {
            Text(if (esAzul) "Cambiar a verde" else "Cambiar a azul")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = automatico, onCheckedChange = { automatico = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cambio automático cada 1.5 s")
        }

        // Comparación: cambio directo vs. tween vs. spring
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MuestraColor(
                titulo = "Sin animación",
                detalle = "instantáneo",
                color = colorDestino,
                modifier = Modifier.weight(1f)
            )
            MuestraColor(
                titulo = "tween",
                detalle = "1000 ms lineal",
                color = colorTween,
                modifier = Modifier.weight(1f)
            )
            MuestraColor(
                titulo = "spring",
                detalle = "física, sin duración fija",
                color = colorSpring,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MuestraColor(
    titulo: String,
    detalle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(titulo, style = MaterialTheme.typography.labelLarge)
        Text(
            text = detalle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
