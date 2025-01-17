package com.example.smilz_pdm_pa.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EscalasRepository {

    private val db = FirebaseFirestore.getInstance()

    // Salvar escala
    fun guardarEscala(userId: String, data: String, horario: String): Boolean {
        return try {
            val escalaData = mapOf("data" to data, "horario" to horario)
            db.collection("escalas").document(userId).collection("escalaDetails").add(escalaData)
                .addOnSuccessListener {
                    Log.d("Escala", "Escala guardada com sucesso!")
                }
                .addOnFailureListener { e ->
                    Log.e("Escala", "Erro ao guardar escala: ${e.message}")
                }
            true
        } catch (e: Exception) {
            Log.e("Escala", "Erro ao guardar escala: ${e.message}")
            false
        }
    }


    // Obter escalas para um usuário específico
    suspend fun obterEscalasPorUser(userId: String): List<Map<String, Any>> {
        return try {
            val querySnapshot = db.collection("escalas")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            querySnapshot.documents.map { it.data ?: emptyMap() }
        } catch (e: Exception) {
            Log.e("EscalaError", "Erro ao buscar escalas: ${e.message}", e)
            emptyList()
        }
    }

    // Obter todas as escalas (apenas para Admin ou VoluntárioLíder)
    suspend fun obterTodasAsEscalas(): List<Map<String, Any>> {
        return try {
            val querySnapshot = db.collection("escalas").get().await()
            querySnapshot.documents.map { it.data ?: emptyMap() }
        } catch (e: Exception) {
            Log.e("EscalaError", "Erro ao buscar todas as escalas: ${e.message}", e)
            emptyList()
        }
    }
}
