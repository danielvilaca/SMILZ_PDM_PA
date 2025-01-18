package com.example.smilz_pdm_pa.model

data class VoluntarioModel(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val role: String? = "",
    val dataRegisto: String = ""
)
