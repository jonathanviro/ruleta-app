package com.javr.ruleta

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.javr.ruleta.databinding.ActivityRuletaBinding
import com.javr.ruleta.util.GlobalVariables
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class RuletaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRuletaBinding

    private val inactivityDuration = 120L // 60 segundos
    private val handler = Handler(Looper.getMainLooper())
    private var inactivityStartTime: Long = 0
    private val INTENTO_GANADOR = 3
    private val INTENTO_SIGUE_PARTICIPANDO = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRuletaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCompenents()
    }

    private fun initCompenents() {
        val vueltaCompleta = 360f * 8
        val gradosPremio = vueltaCompleta + 15
        val gradosGraciasParticipar = vueltaCompleta + 45
        val gradosSigueParticipando = vueltaCompleta + 102

        var rotateAnimator: ObjectAnimator
        var giro = 0f

        binding.btnFooterGirarRuleta.setOnClickListener {
            Log.i("JUGAR", "Se presiono")
            binding.btnFooterGirarRuleta.isEnabled = false
            restartInactivityTimer()

            val numeroAleatorio = Random.nextInt(1, INTENTO_GANADOR)

            var intent = Intent(this, PremioActivity::class.java)

            if (GlobalVariables.contadorGanador == INTENTO_GANADOR) {
                GlobalVariables.contadorGanador = 0
                giro = gradosPremio
                intent = Intent(this, PremioActivity::class.java)
            } else {
                when (numeroAleatorio) {
                    in 1..INTENTO_SIGUE_PARTICIPANDO -> {
                        giro = gradosSigueParticipando
                    }

                    in (INTENTO_SIGUE_PARTICIPANDO +1 )..(INTENTO_GANADOR - 1) -> {
                        GlobalVariables.contadorGanador = GlobalVariables.contadorGanador + 1
                        giro = gradosGraciasParticipar
                        intent = Intent(this, MainActivity::class.java)
                    }
                }
            }

            rotateAnimator = ObjectAnimator.ofFloat(binding.ruleta, "rotation", 0f, giro)
            rotateAnimator.duration = 4000

            rotateAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    if (GlobalVariables.contadorGanador == INTENTO_GANADOR) {
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    } else {
                        when (numeroAleatorio) {
                            in 1..INTENTO_SIGUE_PARTICIPANDO -> {
                                binding.btnFooterGirarRuleta.isEnabled = true
                            }

                            in (INTENTO_SIGUE_PARTICIPANDO +1 )..(INTENTO_GANADOR - 1) -> {
                                Thread.sleep(2000)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                finish()
                            }
                        }
                    }
                }

                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })

            rotateAnimator.start()
        }
    }

    private fun startInactivityTimer() {
        inactivityStartTime = System.currentTimeMillis()
        handler.postDelayed(inactivityRunnable, TimeUnit.SECONDS.toMillis(inactivityDuration))
    }

    private val inactivityRunnable = Runnable {
        val elapsedTime = System.currentTimeMillis() - inactivityStartTime
        if (elapsedTime >= TimeUnit.SECONDS.toMillis(inactivityDuration)) {
            val intent = Intent(this@RuletaActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    private fun restartInactivityTimer() {
        handler.removeCallbacks(inactivityRunnable)
        startInactivityTimer()
    }

    private fun stopInactivityTimer() {
        handler.removeCallbacks(inactivityRunnable)
    }

    override fun onResume() {
        super.onResume()
        restartInactivityTimer()
    }

    override fun onPause() {
        super.onPause()
        stopInactivityTimer()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        restartInactivityTimer()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        restartInactivityTimer()
        return super.dispatchTouchEvent(event)
    }
}