package com.example.smilz_pdm_pa.model

import java.io.Serializable

data class Relatorio(
    val ano: String,
    val mes: String,
    val nacionalidade: String,
    val numeroVisitas: Int,
    val dadosGrafico: Map<String, Int>
) : Serializable
