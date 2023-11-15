package com.javr.ruleta

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.javr.ruleta.databinding.ActivityRuletaBinding
import java.util.Calendar

class RuletaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRuletaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRuletaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCompenents()
    }

    private fun initCompenents() {
        val ivRuleta = binding.ruleta
        val vueltaCompleta = 360f * 8

        // Obtener la hora actual en formato de 24 horas
        val horaActual = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // Calcular el índice de premio en base a la hora actual
        val indicePremio = calcularIndicePremio(horaActual)

        val rotateAnimator = ObjectAnimator.ofFloat(ivRuleta, "rotation", 0f, vueltaCompleta)
        rotateAnimator.duration = 1000

        binding.btnFooterGirarRuleta.setOnClickListener {
            val intent: Intent

            when (indicePremio) {
                in 0..7 -> {
                    // Ganar (índices 0-7)
                    intent = Intent(this, PremioActivity::class.java)
                }
                8 -> {
                    // Seguir Participando (índice 8)
                }
                else -> {
                    // Gracias por Participar (índices 9-23)
                    intent = Intent(this, MainActivity::class.java)
                }
            }

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            rotateAnimator.start()
        }
    }

    private fun calcularIndicePremio(hora: Int): Int {
        // Ejemplo: Dividir el día en 24 segmentos
        val segmentosPorHora = 24
        val indiceGanar = 8 // Número de premios
        val indiceSeguirParticipando = 1 // Índice para seguir participando
        val totalSegmentos = indiceGanar + indiceSeguirParticipando

        // Calcular el índice de premio en base a la hora actual
        val indiceCalculado = (hora * totalSegmentos) / segmentosPorHora

        return if (indiceCalculado < indiceGanar) {
            // Índices 0-7 para Ganar
            indiceCalculado
        } else if (indiceCalculado == indiceGanar) {
            // Índice 8 para Seguir Participando
            indiceGanar
        } else {
            // Índices 9-23 para Gracias por Participar
            indiceCalculado + (segmentosPorHora - totalSegmentos)
        }
    }

}