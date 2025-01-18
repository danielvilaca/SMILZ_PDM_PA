package com.example.smilz_pdm_pa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smilz_pdm_pa.model.Escala
import com.example.smilz_pdm_pa.model.VoluntarioModel
import com.example.smilz_pdm_pa.repository.EscalasRepository
import com.example.smilz_pdm_pa.repository.VoluntarioRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class VoluntarioViewModel : ViewModel() {

    private val voluntarioRepository = VoluntarioRepository()
    private val escalasRepository = EscalasRepository()

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _isUserRegistered = MutableLiveData<Boolean>()
    val isUserRegistered: LiveData<Boolean> get() = _isUserRegistered

    private val _escalas = MutableLiveData<List<Map<String, Any>>>()
    val escalas: LiveData<List<Map<String, Any>>> get() = _escalas

    private val firestore = FirebaseFirestore.getInstance()


    // Login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val userId = voluntarioRepository.loginUser(email, password)
                if (userId != null) {
                    _isUserLoggedIn.value = true
                    carregarEscalasParaUser(userId)
                } else {
                    _isUserLoggedIn.value = false
                }
            } catch (e: Exception) {
                _isUserLoggedIn.value = false
                Log.e("LoginError", "Erro no login: ${e.message}")
            }
        }
    }


    // Registar user
    fun registerUser(voluntario: VoluntarioModel) {
        viewModelScope.launch {
            try {
                val result = voluntarioRepository.registerUser(voluntario)
                _isUserRegistered.value = result
            } catch (e: Exception) {
                _isUserRegistered.value = false
            }
        }
    }

    // Registar escala
    fun registrarEscala(escala: Escala) {
        firestore.collection("escalas")
            .add(escala)
            .addOnSuccessListener {
                Log.d("Escala", "Escala registada com sucesso")
            }
            .addOnFailureListener { e ->
                Log.e("Escala", "Erro ao registar a escala", e)
            }
    }



    fun carregarEscalasParaUser(userId: String) {
        viewModelScope.launch {
            val userEscalas = escalasRepository.obterEscalasPorUser(userId)
            _escalas.value = userEscalas
        }
    }

    fun obterEscalasEmTempoReal(callback: (List<Escala>) -> Unit) {
        val userId = obterUserId()
        if (userId.isNotEmpty()) {
            firestore.collection("escalas")
                .whereEqualTo("id", userId)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.e("Firestore", "Erro ao obter escalas: ", e)
                        callback(emptyList())
                        return@addSnapshotListener
                    }

                    if (snapshots != null && !snapshots.isEmpty) {
                        val escalas = snapshots.map { doc ->
                            Escala(
                                data = doc.getString("data") ?: "",
                                horario = doc.getString("horario") ?: "",
                                voluntarioId = doc.getString("voluntarioId") ?: "",
                                voluntarioNome = doc.getString("nome") ?: "",
                                criadoEm = doc.getString("criadoEm") ?: ""
                            )
                        }
                        callback(escalas)
                    } else {
                        callback(emptyList())
                    }
                }
        } else {
            callback(emptyList())
        }
    }



    private fun obterUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""
    }



}
