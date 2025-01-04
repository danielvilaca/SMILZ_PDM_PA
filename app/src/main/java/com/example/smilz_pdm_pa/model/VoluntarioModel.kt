package com.example.smilz_pdm_pa.model

data class VoluntarioModel(
    val id: String = "",             // ID único do voluntário (usaremos o UID do Firebase)
    val nome: String = "",           // Nome completo
    val email: String = "",          // Email (usado para login)
    val senha: String = "",          // Senha (não vamos armazenar diretamente aqui, o Firebase Auth cuida disso)
    val role: String = "voluntario", // Papel (admin ou voluntário normal)
    val dataRegisto: String = ""     // Data de registo
)
