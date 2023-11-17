package com.javr.ruleta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.javr.ruleta.databinding.ActivityPremioBinding

class PremioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPremioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCompenents()
    }

    private fun initCompenents() {
        binding.btnFooterIrInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}