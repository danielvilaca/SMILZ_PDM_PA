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

    // MutableLiveData para gerenciar o estado de erro ou sucesso do login
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> get() = _loginStatus

    // MutableLiveData para gerenciar o estado de sucesso ou falha no registo
    private val _isUserRegistered = MutableLiveData<Boolean>()
    val isUserRegistered: LiveData<Boolean> get() = _isUserRegistered

    // Função de login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                // Chama o repositório para fazer o login
                val result = voluntarioRepository.loginUser(email, password)
                if (result) {
                    // Login bem-sucedido
                    _loginStatus.value = "Login bem-sucedido"
                } else {
                    // Login falhou
                    _loginStatus.value = "Falha no login. Verifique as credenciais."
                }
            } catch (e: Exception) {
                // Em caso de erro
                _loginStatus.value = "Erro: ${e.message}"
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
