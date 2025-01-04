package com.example.smilz_pdm_pa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.example.smilz_pdm_pa.repository.BeneficiarioRepository
import kotlinx.coroutines.launch

class BeneficiarioViewModel : ViewModel() {

    private val beneficiarioRepository = BeneficiarioRepository()

    // LiveData para manter a lista de beneficiários
    private val _beneficiarios = MutableLiveData<List<BeneficiarioModel>>()
    val beneficiarios: LiveData<List<BeneficiarioModel>> get() = _beneficiarios

    // Função para carregar a lista de beneficiários
    fun loadBeneficiarios() {
        viewModelScope.launch {
            beneficiarioRepository.getBeneficiarios { lista ->
                _beneficiarios.postValue(lista)
            }
        }
    }

    // Função para adicionar um novo beneficiário
    fun addBeneficiario(beneficiario: BeneficiarioModel) {
        viewModelScope.launch {
            beneficiarioRepository.saveBeneficiario(beneficiario) { success ->
                if (success) {
                    loadBeneficiarios()  // Recarrega a lista após salvar
                }
            }
        }
    }
}
