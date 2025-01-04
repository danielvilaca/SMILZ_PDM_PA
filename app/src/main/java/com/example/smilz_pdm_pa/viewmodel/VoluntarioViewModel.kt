package com.example.smilz_pdm_pa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smilz_pdm_pa.model.VoluntarioModel
import com.example.smilz_pdm_pa.repository.VoluntarioRepository
import kotlinx.coroutines.launch

class VoluntarioViewModel : ViewModel() {

    private val voluntarioRepository = VoluntarioRepository()

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    // MutableLiveData para gerenciar o estado de sucesso ou falha no registo
    private val _isUserRegistered = MutableLiveData<Boolean>()
    val isUserRegistered: LiveData<Boolean> get() = _isUserRegistered

    //Login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = voluntarioRepository.loginUser(email, password)
                _isUserLoggedIn.value = result
            } catch (e: Exception) {
                _isUserLoggedIn.value = false
            }
        }
    }

    // Função para registar um voluntário
    fun registerUser(voluntario: VoluntarioModel) {
        viewModelScope.launch {
            try {
                // Chama o repositório para registrar o voluntário
                val result = voluntarioRepository.registerUser(voluntario)
                _isUserRegistered.value = result
            } catch (e: Exception) {
                _isUserRegistered.value = false
            }
        }
    }
}
