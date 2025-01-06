package com.example.smilz_pdm_pa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.example.smilz_pdm_pa.repository.BeneficiarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BeneficiarioViewModel : ViewModel() {

    private val repository = BeneficiarioRepository()

    private val _beneficiarios = MutableStateFlow<List<BeneficiarioModel>>(emptyList())
    val beneficiarios: StateFlow<List<BeneficiarioModel>> = _beneficiarios

    fun fetchBeneficiarios() {
        viewModelScope.launch {
            _beneficiarios.value = repository.getBeneficiarios()
        }
    }

    fun addBeneficiario(beneficiario: BeneficiarioModel) {
        viewModelScope.launch {
            repository.addBeneficiario(beneficiario)
            fetchBeneficiarios() // Atualizar lista
        }
    }

    fun deleteBeneficiario(id: String) {
        viewModelScope.launch {
            repository.deleteBeneficiario(id)
            fetchBeneficiarios()
        }
    }

    // Função para excluir todos os beneficiários
    fun deleteAllBeneficiarios() {
        viewModelScope.launch {
            repository.deleteAllBeneficiarios()
            fetchBeneficiarios() // Atualiza a lista após exclusão
        }
    }

    fun updateBeneficiario(beneficiario: BeneficiarioModel, callback: (Boolean) -> Unit) {
        repository.updateBeneficiario(beneficiario, callback)
    }
}
