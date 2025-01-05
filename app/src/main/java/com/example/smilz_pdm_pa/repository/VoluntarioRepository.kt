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
    suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            // Autenticar o usuário no Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            // Verificar se o usuário existe no Firestore
            val user = authResult.user
            user?.let {
                val document = db.collection("voluntarios").document(user.uid).get().await()

                if (document.exists()) {
                    Log.d("Login", "Usuário encontrado no Firestore")
                } else {
                    Log.d("Login", "Usuário não encontrado no Firestore")
                }
                document.exists()
            } ?: false
        } catch (e: Exception) {
            Log.e("LoginError", "Erro ao fazer login: ${e.message}", e)
            return false
        }
    }

    // Função de registo do usuário
    suspend fun registerUser(voluntario: VoluntarioModel): Boolean {
        return try {
            // Registra o usuário no Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(voluntario.email, voluntario.senha).await()

            // Salva os dados do voluntário no Firestore
            val user = authResult.user
            user?.let {
                val voluntarioData = mapOf(
                    "nome" to voluntario.nome,
                    "email" to voluntario.email,
                    "role" to voluntario.role,
                    "dataRegisto" to voluntario.dataRegisto
                )

                // Salva os dados no Firestore
                db.collection("voluntarios").document(user.uid).set(voluntarioData).await()

                true
            } ?: false
        } catch (e: Exception) {
            Log.e("RegisterError", "Erro ao registar usuário: ${e.message}", e)
            false
        }
    }
}
