package com.sebastianalvaradoz.laboratorio13_animaciones.juego

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.random.Random

// ---------- Configuración y colores del juego ----------

enum class FaseJuego { Inicio, Jugando, Fin }

private const val DURACION_PARTIDA_MS = 30_000
private const val ALARMA_MS = 5_000

private val FondoNoche = Brush.verticalGradient(
    listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
)
private val Dorado = Color(0xFFFFD54F)
private val TextoOscuro = Color(0xFF1A1A2E)
private val VerdeTiempo = Color(0xFF66BB6A)
private val AmbarTiempo = Color(0xFFFFCA28)
private val RojoTiempo = Color(0xFFEF5350)

// ---------- Pantalla principal del juego ----------

@Composable
fun JuegoAtrapaLaEstrella() {
    var fase by rememberSaveable { mutableStateOf(FaseJuego.Inicio) }
    var puntos by rememberSaveable { mutableIntStateOf(0) }
    var record by rememberSaveable { mutableIntStateOf(0) }
    var nuevoRecord by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(FondoNoche)
    ) {
        CieloEstrellado()

        // AnimatedContent: transición entre Inicio → Jugando → Fin
        AnimatedContent(
            targetState = fase,
            transitionSpec = {
                (fadeIn(tween(500, delayMillis = 150)) +
                    scaleIn(tween(500, delayMillis = 150), initialScale = 0.85f)) togetherWith
                    (fadeOut(tween(250)) + scaleOut(tween(250), targetScale = 1.1f))
            },
            label = "faseJuego",
            modifier = Modifier.fillMaxSize()
        ) { faseActual ->
            when (faseActual) {
                FaseJuego.Inicio -> PantallaInicio(
                    record = record,
                    onJugar = {
                        puntos = 0
                        fase = FaseJuego.Jugando
                    }
                )

                FaseJuego.Jugando -> PantallaJugando(
                    puntos = puntos,
                    onPuntos = { premio -> puntos += premio },
                    onFin = {
                        nuevoRecord = puntos > record
                        if (nuevoRecord) record = puntos
                        fase = FaseJuego.Fin
                    }
                )

                FaseJuego.Fin -> PantallaFin(
                    puntos = puntos,
                    record = record,
                    nuevoRecord = nuevoRecord,
                    onReintentar = {
                        puntos = 0
                        fase = FaseJuego.Jugando
                    },
                    onInicio = { fase = FaseJuego.Inicio }
                )
            }
        }
    }
}

// ---------- Fase 1: Inicio ----------

@Composable
private fun PantallaInicio(record: Int, onJugar: () -> Unit) {
    // Animación infinita: la estrella flota arriba y abajo
    val flotar = rememberInfiniteTransition(label = "flotar")
    val desplazamiento by flotar.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "desplazamiento"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⭐", fontSize = 96.sp, modifier = Modifier.offset(y = desplazamiento.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Atrapa la Estrella",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Toca la estrella antes de que escape.\nTienes 30 segundos y cada nivel es más rápido.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.75f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (record > 0) {
            Text(
                text = "🏆 Récord: $record",
                style = MaterialTheme.typography.titleMedium,
                color = Dorado
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        BotonDorado(texto = "JUGAR", onClick = onJugar)
    }
}

// ---------- Fase 2: Jugando ----------

@Composable
private fun PantallaJugando(
    puntos: Int,
    onPuntos: (Int) -> Unit,
    onFin: () -> Unit
) {
    val onFinActual by rememberUpdatedState(onFin)

    var tiempoRestante by remember { mutableIntStateOf(DURACION_PARTIDA_MS) }
    var aciertos by remember { mutableIntStateOf(0) }
    var racha by remember { mutableIntStateOf(0) }
    var ultimoPremio by remember { mutableIntStateOf(10) }

    // Posición de la estrella como fracción (0..1) del área de juego
    var fx by remember { mutableFloatStateOf(0.5f) }
    var fy by remember { mutableFloatStateOf(0.5f) }
    var saltos by remember { mutableIntStateOf(0) }

    // Dónde se atrapó la última estrella (para el texto "+10")
    var premioFx by remember { mutableFloatStateOf(0.5f) }
    var premioFy by remember { mutableFloatStateOf(0.5f) }
    var mostrarPremio by remember { mutableStateOf(false) }

    val nivel = 1 + aciertos / 5
    val vidaEstrellaMs = (1600 - nivel * 150).coerceAtLeast(650)

    fun saltar() {
        fx = Random.nextFloat()
        fy = Random.nextFloat()
        saltos++
    }

    // Cronómetro de la partida
    LaunchedEffect(Unit) {
        while (tiempoRestante > 0) {
            delay(100)
            tiempoRestante -= 100
        }
        onFinActual()
    }

    // Si no la atrapas a tiempo, la estrella escapa y se pierde la racha
    LaunchedEffect(saltos) {
        delay(vidaEstrellaMs.toLong())
        if (tiempoRestante > 0) {
            racha = 0
            saltar()
        }
    }

    // Muestra el "+10" un instante después de cada acierto
    LaunchedEffect(aciertos) {
        if (aciertos > 0) {
            mostrarPremio = true
            delay(450)
            mostrarPremio = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Marcador superior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TarjetaMarcador(titulo = "Puntos", modifier = Modifier.weight(1f)) {
                // AnimatedContent: el número sube al cambiar
                AnimatedContent(
                    targetState = puntos,
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn()) togetherWith
                            (slideOutVertically { -it } + fadeOut())
                    },
                    label = "puntos"
                ) { valor ->
                    TextoMarcador("$valor")
                }
            }
            TarjetaMarcador(titulo = "Nivel", modifier = Modifier.weight(1f)) {
                TextoMarcador("$nivel")
            }
            TarjetaMarcador(titulo = "Tiempo", modifier = Modifier.weight(1f)) {
                TextoMarcador("${ceil(tiempoRestante / 1000f).toInt()} s")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        BarraTiempo(fraccion = tiempoRestante / DURACION_PARTIDA_MS.toFloat())
        Spacer(modifier = Modifier.height(12.dp))

        AreaDeJuego(
            fx = fx,
            fy = fy,
            nivel = nivel,
            racha = racha,
            alarma = tiempoRestante <= ALARMA_MS,
            mostrarPremio = mostrarPremio,
            premio = ultimoPremio,
            premioFx = premioFx,
            premioFy = premioFy,
            onAtrapar = {
                if (tiempoRestante > 0) {
                    premioFx = fx
                    premioFy = fy
                    racha++
                    aciertos++
                    // Bonus: +5 por cada 3 aciertos seguidos
                    ultimoPremio = 10 + (racha / 3) * 5
                    onPuntos(ultimoPremio)
                    saltar()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun AreaDeJuego(
    fx: Float,
    fy: Float,
    nivel: Int,
    racha: Int,
    alarma: Boolean,
    mostrarPremio: Boolean,
    premio: Int,
    premioFx: Float,
    premioFy: Float,
    onAtrapar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // animateColorAsState: el área se tiñe de rojo en los últimos 5 segundos
    val colorArea by animateColorAsState(
        targetValue = if (alarma) Color(0x55FF1744) else Color(0x1AFFFFFF),
        animationSpec = tween(400),
        label = "colorArea"
    )

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(colorArea)
    ) {
        // animateDpAsState: la estrella se hace más pequeña al subir de nivel
        val tamanoDestino = (76 - nivel * 5).coerceAtLeast(44).dp
        val tamano by animateDpAsState(tamanoDestino, tween(400), label = "tamano")

        // animateDpAsState + offset: la estrella salta a su nueva posición con rebote
        val resorte = spring<Dp>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
        val x by animateDpAsState((maxWidth - tamanoDestino) * fx, resorte, label = "x")
        val y by animateDpAsState((maxHeight - tamanoDestino) * fy, resorte, label = "y")

        // Animación infinita: la estrella late y se balancea
        val pulso = rememberInfiniteTransition(label = "pulso")
        val escala by pulso.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
            label = "escala"
        )
        val giro by pulso.animateFloat(
            initialValue = -12f,
            targetValue = 12f,
            animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
            label = "giro"
        )

        Box(
            modifier = Modifier
                .offset(x = x, y = y)
                .size(tamano)
                .graphicsLayer {
                    scaleX = escala
                    scaleY = escala
                    rotationZ = giro
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAtrapar
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("⭐", fontSize = (tamano.value * 0.75f).sp)
        }

        // AnimatedVisibility: texto "+10" que sube y se desvanece donde se atrapó la estrella
        AnimatedVisibility(
            visible = mostrarPremio,
            enter = fadeIn(tween(150)) + slideInVertically { it },
            exit = fadeOut(tween(300)) + slideOutVertically { -it },
            modifier = Modifier.offset(
                x = (maxWidth - tamanoDestino) * premioFx,
                y = (maxHeight - tamanoDestino) * premioFy - 24.dp
            )
        ) {
            Text(
                text = "+$premio",
                color = Dorado,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }

        // AnimatedVisibility: aviso de racha cuando llevas 3 o más seguidas
        AnimatedVisibility(
            visible = racha >= 3,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        ) {
            Text(
                text = "🔥 Racha x$racha",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFFF7043))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun BarraTiempo(fraccion: Float) {
    val ancho by animateFloatAsState(
        targetValue = fraccion.coerceIn(0f, 1f),
        animationSpec = tween(100, easing = LinearEasing),
        label = "anchoBarra"
    )
    // animateColorAsState: verde → ámbar → rojo según el tiempo restante
    val color by animateColorAsState(
        targetValue = when {
            fraccion > 0.5f -> VerdeTiempo
            fraccion > 0.2f -> AmbarTiempo
            else -> RojoTiempo
        },
        animationSpec = tween(500),
        label = "colorBarra"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(ancho)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

// ---------- Fase 3: Fin ----------

@Composable
private fun PantallaFin(
    puntos: Int,
    record: Int,
    nuevoRecord: Boolean,
    onReintentar: () -> Unit,
    onInicio: () -> Unit
) {
    // El puntaje "cuenta" desde 0 hasta el resultado final
    var objetivo by remember { mutableIntStateOf(0) }
    var mostrarRecord by remember { mutableStateOf(false) }
    val puntosMostrados by animateIntAsState(
        targetValue = objetivo,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "conteo"
    )

    LaunchedEffect(Unit) {
        delay(300)
        objetivo = puntos
        delay(1300)
        mostrarRecord = nuevoRecord
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⏰", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "¡Se acabó el tiempo!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "$puntosMostrados",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = Dorado
        )
        Text("puntos", color = Color.White.copy(alpha = 0.75f))
        Spacer(modifier = Modifier.height(16.dp))

        // AnimatedVisibility directo dentro del Column (aquí sí es válido)
        AnimatedVisibility(
            visible = mostrarRecord,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioHighBouncy)) + fadeIn()
        ) {
            Text(
                text = "🏆 ¡Nuevo récord!",
                fontWeight = FontWeight.Bold,
                color = TextoOscuro,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Dorado)
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            )
        }
        if (!nuevoRecord && record > 0) {
            Text(
                text = "Récord: $record",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        BotonDorado(texto = "JUGAR DE NUEVO", onClick = onReintentar)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onInicio,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Volver al inicio")
        }
    }
}

// ---------- Componentes reutilizables ----------

@Composable
private fun CieloEstrellado() {
    // Fondo decorativo: estrellas pequeñas que titilan suavemente
    val titileo = rememberInfiniteTransition(label = "titileo")
    val brillo by titileo.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "brillo"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val aleatorio = Random(42) // semilla fija: las estrellas siempre en el mismo lugar
        repeat(70) {
            drawCircle(
                color = Color.White.copy(alpha = (aleatorio.nextFloat() * 0.6f + 0.2f) * brillo),
                radius = (aleatorio.nextFloat() * 1.5f + 0.5f).dp.toPx(),
                center = Offset(
                    x = aleatorio.nextFloat() * size.width,
                    y = aleatorio.nextFloat() * size.height
                )
            )
        }
    }
}

@Composable
private fun TarjetaMarcador(
    titulo: String,
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f)
        )
        contenido()
    }
}

@Composable
private fun TextoMarcador(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
private fun BotonDorado(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Dorado, contentColor = TextoOscuro),
        modifier = Modifier
            .height(52.dp)
            .width(220.dp)
    ) {
        Text(texto, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
    }
}
