package com.example.smilz_pdm_pa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smilz_pdm_pa.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.loginUser(email, password)
                _currentUser.value = user
            } catch (e: Exception) {
                // Lidar com o erro, por exemplo, mostrar uma mensagem de erro
                _currentUser.value = null
                // Exemplo de erro:
                Log.e("AuthViewModel", "Erro ao fazer login", e)
            }
        }
    }

    fun registerUser(email: String, password: String) {
        // Lança a função de registro dentro de uma corrotina
        viewModelScope.launch {
            val user = authRepository.registerUser(email, password)
            _currentUser.value = user
        }
    }

    fun logout() {
        authRepository.signOut()
        _currentUser.value = null
    }

    fun getCurrentUser() {
        _currentUser.value = authRepository.getCurrentUser()
    }
}
