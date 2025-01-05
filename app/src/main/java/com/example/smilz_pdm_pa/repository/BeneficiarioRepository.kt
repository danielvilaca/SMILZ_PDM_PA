package com.example.smilz_pdm_pa.repository

import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BeneficiarioRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("beneficiarios")

    suspend fun addBeneficiario(beneficiario: BeneficiarioModel) {
        collection.add(beneficiario).await()
    }

    suspend fun getBeneficiarios(): List<BeneficiarioModel> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(BeneficiarioModel::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun deleteBeneficiario(id: String) {
        collection.document(id).delete().await()
    }

    suspend fun updateBeneficiario(id: String, beneficiario: BeneficiarioModel) {
        collection.document(id).set(beneficiario).await()
    }

    suspend fun getBeneficiarioById(id: String): BeneficiarioModel? {
        val doc = collection.document(id).get().await()
        return doc.toObject(BeneficiarioModel::class.java)?.copy(id = doc.id)
    }

}
