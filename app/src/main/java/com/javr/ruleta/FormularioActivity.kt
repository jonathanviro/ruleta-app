package com.javr.ruleta

import ExcelUtil
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.javr.ruleta.databinding.ActivityFormularioBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class FormularioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormularioBinding
    private lateinit var etNombre: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etEmail: EditText
    private lateinit var excelUtil: ExcelUtil

    private val inactivityDuration = 120L // 60 segundos
    private val handler = Handler(Looper.getMainLooper())
    private var inactivityStartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        startInactivityTimer()
    }

    private fun initComponents() {
        etNombre = binding.etNombre
        etTelefono = binding.etTelefono
        etEmail = binding.etEmail

        binding.btnFooterComencemos.setOnClickListener {
            if (validar()) {
                generateExcel()
            }
        }
    }

    private fun validar(): Boolean {
        if (etNombre.text.toString().isBlank()) {
            etNombre.hint = "Por favor, ingrese su nombre"
            etNombre.requestFocus()
            return false
        }

        if (etTelefono.text.toString().isBlank()) {
            etTelefono.hint = "Por favor, ingrese su teléfono"
            etTelefono.requestFocus()
            return false
        }

        if (etEmail.text.toString().isBlank()) {
            etEmail.hint = "Por favor, ingrese su correo electrónico"
            etEmail.requestFocus()
            return false
        }

        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val email = etEmail.text.toString().trim()

        if (email.isBlank() || !email.matches(emailRegex.toRegex())) {
            etEmail.text.clear()
            etEmail.hint = "Por favor, ingrese un correo electrónico válido"
            etEmail.requestFocus()
            return false
        }

        return true
    }


    private fun goToRuleta() {
        val intent = Intent(this, RuletaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish()
    }

    private fun generateExcel() {
        binding.btnFooterComencemos.isEnabled = false
        createExcelUtil()
        val datos = arrayOf(
            etNombre.text.toString().trim(),
            etTelefono.text.toString().trim(),
            etEmail.text.toString().trim()
        )
        excelUtil.addData(datos)
        excelUtil.save()
        goToRuleta()
    }

    private fun createExcelUtil() {
        val carpetaDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + "/ruleta/"
        val fechaActual = SimpleDateFormat("yyyyMMdd").format(Date())
        val nombreArchivo = "datos_ruleta_$fechaActual.xlsx"
        val rutaCompleta = carpetaDescargas + nombreArchivo

        // Verificar y crear la carpeta si no existe
        val carpetaDescargasFile = File(carpetaDescargas)
        if (!carpetaDescargasFile.exists()) {
            carpetaDescargasFile.mkdirs()
        }

        val archivoExcel = File(rutaCompleta)
        if (archivoExcel.exists()) {
            // Si existe, utilizar la instancia existente de ExcelUtil
            excelUtil = ExcelUtil(rutaCompleta)
        } else {
            // Si no existe, crear una nueva instancia de ExcelUtil
            excelUtil = ExcelUtil(rutaCompleta)
        }
    }

    private fun screenWait() {
        // Muestra la vista semitransparente y la animación de Lottie
        binding.viewOverlay.visibility = View.VISIBLE
        binding.lottieAnimationView.visibility = View.VISIBLE

        // Configura y reproduce la animación de Lottie
        binding.lottieAnimationView.setAnimation(R.raw.lottie_wait)
        binding.lottieAnimationView.playAnimation()
    }

    /*val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = dateFormat.format(Date())
    val csvFileName = "datos_ruleta_$today.csv"

    val csvManager = CsvManager(csvFileName)

    // Ejemplo de cómo agregar datos a una nueva fila en el archivo CSV
    csvManager.addDataToCsv(etNombre.text.toString().trim(), etTelefono.text.toString().trim(), etEmail.text.toString().trim())*/

    private fun startInactivityTimer() {
        inactivityStartTime = System.currentTimeMillis()
        handler.postDelayed(inactivityRunnable, TimeUnit.SECONDS.toMillis(inactivityDuration))
    }

    private val inactivityRunnable = Runnable {
        val elapsedTime = System.currentTimeMillis() - inactivityStartTime
        if (elapsedTime >= TimeUnit.SECONDS.toMillis(inactivityDuration)) {
            val intent = Intent(this@FormularioActivity, MainActivity::class.java)
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