package com.sebastianalvaradoz.laboratorio13_animaciones.navegacion

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MenuPrincipal(
    ejercicios: List<Ejercicio>,
    onSeleccionar: (Int) -> Unit
) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Cabecera(cantidad = ejercicios.count { !it.destacado }) }

            itemsIndexed(ejercicios) { indice, ejercicio ->
                // Las tarjetas entran una tras otra con un pequeño retraso
                EntradaEscalonada(indice = indice) {
                    if (ejercicio.destacado) {
                        TarjetaJuego(ejercicio = ejercicio, onClick = { onSeleccionar(indice) })
                    } else {
                        TarjetaEjercicio(ejercicio = ejercicio, onClick = { onSeleccionar(indice) })
                    }
                }
            }
        }
    }
}

@Composable
private fun Cabecera(cantidad: Int) {
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Text(
            text = "Laboratorio 13",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Animaciones en Jetpack Compose",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Etiqueta("Sebastian Alvarado")
            Etiqueta("$cantidad ejercicios + juego")
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

// Tarjeta de cada ejercicio: degradado propio, ícono, número y descripción
@Composable
private fun TarjetaEjercicio(ejercicio: Ejercicio, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(ejercicio.colores))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono dentro de un círculo translúcido
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Text(ejercicio.icono, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ejercicio.etiqueta.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = ejercicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = ejercicio.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            Text(
                text = "›",
                fontSize = 32.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

// Tarjeta especial del juego: más grande, con degradado nocturno y botón dorado
@Composable
private fun TarjetaJuego(ejercicio: Ejercicio, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(ejercicio.colores))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🎮 ${ejercicio.etiqueta.uppercase()} FINAL",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFFFFD54F)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ejercicio.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ejercicio.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "JUGAR ▶",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFFFD54F))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Text(ejercicio.icono, fontSize = 56.sp)
        }
    }
}

// Animación de entrada: cada tarjeta aparece deslizándose desde abajo, una tras otra
@Composable
private fun EntradaEscalonada(indice: Int, contenido: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(indice * 90L)
        visible = true
    }
    val opacidad by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(400),
        label = "opacidadTarjeta"
    )
    val desplazamiento by animateDpAsState(
        targetValue = if (visible) 0.dp else 24.dp,
        animationSpec = tween(400),
        label = "desplazamientoTarjeta"
    )
    Box(
        modifier = Modifier
            .offset(y = desplazamiento)
            .graphicsLayer { alpha = opacidad }
    ) {
        contenido()
    }
}
