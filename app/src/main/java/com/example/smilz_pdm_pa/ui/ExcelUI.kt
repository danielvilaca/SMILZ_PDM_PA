package com.example.smilz_pdm_pa.ui

import android.content.Context
import android.net.Uri
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

object ExcelUI {

    // Função para ler o arquivo Excel
    fun readExcelFile(context: Context, fileUri: Uri) {
        val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
        inputStream?.let { stream ->
            val workbook = WorkbookFactory.create(stream)
            val sheet = workbook.getSheetAt(0) // Obtém a primeira aba da planilha

            // Itera pelas linhas e extrai os dados
            for (row in sheet) {
                val beneficiario = BeneficiarioModel(
                    nome = row.getCell(0)?.stringCellValue.orEmpty(),
                    contacto = row.getCell(1)?.stringCellValue.orEmpty(),
                    reference = row.getCell(2)?.stringCellValue.orEmpty(),
                    family = row.getCell(3)?.numericCellValue?.toInt() ?: 0,
                    nationality = row.getCell(4)?.stringCellValue.orEmpty(),
                    notes = row.getCell(5)?.stringCellValue.orEmpty(),
                    requests = row.getCell(6)?.stringCellValue.orEmpty(),
                    numVisitas = row.getCell(7)?.numericCellValue?.toInt() ?: 0
                )

                // Chama a função para salvar no Firebase
                saveBeneficiarioToFirebase(beneficiario)
            }
        }
    }

    // Função para salvar os dados no Firestore
    private fun saveBeneficiarioToFirebase(beneficiario: BeneficiarioModel) {
        val db = FirebaseFirestore.getInstance()
        val beneficiarioData = hashMapOf(
            "nome" to beneficiario.nome,
            "contacto" to beneficiario.contacto,
            "reference" to beneficiario.reference,
            "family" to beneficiario.family,
            "nationality" to beneficiario.nationality,
            "notes" to beneficiario.notes,
            "requests" to beneficiario.requests,
            "numVisitas" to beneficiario.numVisitas
        )

        // Salvar no Firestore
        db.collection("beneficiarios").add(beneficiarioData)
            .addOnSuccessListener {
                // Sucesso ao salvar
            }
            .addOnFailureListener { e ->
                // Erro ao salvar
            }
    }
}