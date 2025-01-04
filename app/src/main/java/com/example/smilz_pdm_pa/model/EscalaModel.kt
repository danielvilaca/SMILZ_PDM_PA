package com.example.smilz_pdm_pa.model

data class EscalaModel(
    val id: String = "",         // ID único da escala
    val data: String = "",       // Data do turno
    val voluntarios: List<String> = listOf() // IDs dos voluntários na escala
)
