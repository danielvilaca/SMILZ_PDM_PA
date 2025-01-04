package com.example.smilz_pdm_pa.repository

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
            // Fazendo o login no Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            // Se o login for bem-sucedido, retornamos true
            authResult.user?.let {
                true
            } ?: false
        } catch (e: Exception) {
            // Em caso de erro (ex: login inválido), retornamos false
            false
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
            false
        }
    }
}
