package com.example.smilz_pdm_pa.model

data class BeneficiarioModel(
    val id: String = "",             // ID único do beneficiário
    val nome: String = "",           // Nome do beneficiário
    val numeroProcesso: String = "", // Número do processo (campo importante do Excel)
    val morada: String = "",         // Morada
    val contacto: String = "",       // Telefone/Contacto
    val numIdas: Map<String, Int> = mapOf() // Nº de idas à loja por ano (2023, 2024...)
)
