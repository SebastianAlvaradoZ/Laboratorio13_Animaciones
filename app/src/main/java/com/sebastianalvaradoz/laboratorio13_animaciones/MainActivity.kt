package com.sebastianalvaradoz.laboratorio13_animaciones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sebastianalvaradoz.laboratorio13_animaciones.navegacion.AppLaboratorio
import com.sebastianalvaradoz.laboratorio13_animaciones.ui.theme.Laboratorio13_AnimacionesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Laboratorio13_AnimacionesTheme {
                AppLaboratorio()
            }
        }
    }
}
