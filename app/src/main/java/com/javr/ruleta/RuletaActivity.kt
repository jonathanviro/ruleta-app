package com.javr.ruleta

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.javr.ruleta.databinding.ActivityRuletaBinding

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

        val rotateAnimator = ObjectAnimator.ofFloat(ivRuleta, "rotation", 0f, vueltaCompleta)
        rotateAnimator.duration = 4000

        binding.btnFooterGirarRuleta.setOnClickListener {
            //val intent = Intent(this, PremioActivity::class.java)
            //startActivity(intent)
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            rotateAnimator.start()
        }
    }
}