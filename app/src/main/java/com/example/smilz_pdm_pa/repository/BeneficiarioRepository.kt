package com.example.smilz_pdm_pa.repository

import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.google.firebase.firestore.FirebaseFirestore

class BeneficiarioRepository {

    private val db = FirebaseFirestore.getInstance()

    // Função para salvar um beneficiário no Firestore
    fun saveBeneficiario(beneficiario: BeneficiarioModel, onComplete: (Boolean) -> Unit) {
        db.collection("beneficiarios")
            .document(beneficiario.id)
            .set(beneficiario)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                onComplete(false)
            }
    }

    // Função para buscar todos os beneficiários
    fun getBeneficiarios(onComplete: (List<BeneficiarioModel>) -> Unit) {
        val beneficiariosList = mutableListOf<BeneficiarioModel>()
        db.collection("beneficiarios").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val beneficiario = document.toObject(BeneficiarioModel::class.java)
                    beneficiariosList.add(beneficiario)
                }
                onComplete(beneficiariosList)
            }
            .addOnFailureListener { e ->
                onComplete(emptyList())
            }
    }
}
