package com.javr.ruleta.util

import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.io.IOException

class CsvManager(private val csvFileName: String) {

    private val csvDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ruleta")
    private val csvFilePath = File(csvDirectory, csvFileName)

    init {
        if (!csvDirectory.exists()) {
            csvDirectory.mkdirs()
        }

        if (!csvFilePath.exists()) {
            // Crea el archivo CSV y escribe la primera fila de encabezados si no existe
            writeCsvHeader()
        }
    }

    private fun writeCsvHeader() {
        try {
            FileWriter(csvFilePath, true).use { writer ->
                writer.append("Columna1,Columna2,Columna3\n") // Reemplaza con tus encabezados
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun addDataToCsv(data1: String, data2: String, data3: String) {
        try {
            FileWriter(csvFilePath, true).use { writer ->
                writer.append("$data1,$data2,$data3\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}


