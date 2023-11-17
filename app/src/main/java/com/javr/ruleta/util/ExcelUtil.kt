import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ExcelUtil(private val filePath: String) {
    private val workbook: Workbook

    init {
        val existingFile = File(filePath)
        if (existingFile.exists()) {
            val fis = FileInputStream(existingFile)
            workbook = WorkbookFactory.create(fis)
            fis.close()
        } else {
            workbook = WorkbookFactory.create(true)
            createHeader()
        }
    }

    private fun createHeader() {
        val sheet = workbook.createSheet("Datos Ruleta")
        val headerStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.YELLOW.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER

            val font = workbook.createFont().apply {
                bold = true
            }
            setFont(font)
        }

        val headerRow = sheet.createRow(0)
        val headers = arrayOf("NOMBRE", "TELEFONO", "EMAIL")

        headers.forEachIndexed { index, header ->
            val cell = headerRow.createCell(index)
            cell.setCellValue(header)
            cell.cellStyle = headerStyle
        }
    }

    fun addData(datos: Array<String>) {
        val sheet = workbook.getSheetAt(0) // Obtener la primera hoja
        val rowIndex = sheet.lastRowNum + 1
        val dataRow = sheet.createRow(rowIndex)
        datos.forEachIndexed { index, dato ->
            val cell = dataRow.createCell(index)
            cell.setCellValue(dato)
        }
    }

    fun save() {
        val outputStream = FileOutputStream(filePath)
        workbook.write(outputStream)
        outputStream.close()
        println("Archivo Excel actualizado en: $filePath")
    }
}
