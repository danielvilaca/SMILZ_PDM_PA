package com.example.smilz_pdm_pa.model

data class BeneficiarioModel(
    val id: String = "",
    val nome: String = "",
    val contacto: String? = "",
    val reference: String? = "",
    val family: Int? = 0,
    val nationality: String? = "",
    val notes: String? = "",
    val requests: String? = "",
    val numVisitas: Int? = 0
)
