package com.example.smilz_pdm_pa.repository

import android.util.Log
import com.example.smilz_pdm_pa.model.VoluntarioModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VoluntarioRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Função de login
    suspend fun loginUser(email: String, password: String): String? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            user?.let {
                val document = db.collection("voluntarios").document(user.uid).get().await()
                if (document.exists()) {
                    Log.d("Login", "Utilizador encontrado!")
                } else {
                    Log.d("Login", "Utilizador não encontrado!")
                }
                return user.uid
            }
            null
        } catch (e: Exception) {
            Log.e("LoginError", "Erro ao fazer login: ${e.message}", e)
            null
        }
    }


    // Função de Registo do user
    suspend fun registerUser(voluntario: VoluntarioModel): Boolean {
        return try {
            // Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(voluntario.email, voluntario.senha).await()

            // Dados Firestore
            val user = authResult.user
            user?.let {
                val voluntarioData = mapOf(
                    "nome" to voluntario.nome,
                    "email" to voluntario.email,
                    "role" to voluntario.role,
                    "dataRegisto" to voluntario.dataRegisto
                )

                // Guarda no Firestore
                db.collection("voluntarios").document(user.uid).set(voluntarioData).await()

                true
            } ?: false
        } catch (e: Exception) {
            Log.e("RegisterError", "Erro ao registar utilizador: ${e.message}", e)
            false
        }
    }

    suspend fun guardarEscala(userId: String, data: String, horario: String): Boolean {
        return try {
            val escalaData = mapOf(
                "data" to data,
                "horario" to horario
            )

            db.collection("escalas")
                .document(userId)
                .collection("horarios") // Subcoleção para escalas de cada utilizador
                .add(escalaData)
                .await()

            Log.d("Escala", "Escala guardada com sucesso!")
            true
        } catch (e: Exception) {
            Log.e("EscalaError", "Erro ao guardar escala: ${e.message}", e)
            false
        }
    }


}
