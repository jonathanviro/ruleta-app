package com.javr.ruleta

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.javr.ruleta.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permiso concedido, realiza la operación que necesitas
            } else {
                // Permiso denegado, puedes informar al usuario o manejar la situación de otra manera
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si se tienen los permisos
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permiso ya concedido, realiza la operación que necesitas
            val directorio = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            // Realiza las operaciones necesarias en el directorio
        } else {
            // Si no se tienen los permisos, solicitarlos
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        initComponents()
    }

    private fun initComponents() {
        binding.btnFooterJuguemos.setOnClickListener {
            val intent = Intent(this, FormularioActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}