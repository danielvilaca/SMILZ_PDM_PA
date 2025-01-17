package com.example.smilz_pdm_pa.model

import java.io.Serializable

data class Relatorio(
    val ano: String,  // Atualizando para String
    val mes: String,
    val nacionalidade: String,  // Caso você tenha esse campo, precisa garantir que seja passado corretamente
    val numeroVisitas: Int,
    val dadosGrafico: Map<String, Int> // Mapa de nacionalidades e seus respectivos valores
) : Serializable
