package com.example.smilz_pdm_pa.model

data class BeneficiarioModel(
    val id: String = "",
    val nome: String = "",
    val contacto: String = "",
    val reference: String = "",
    val family: Int? = 0,
    val nationality: String = "",
    val requests: String = "",
    val notes: String = "",
    val numVisitas: Int? = 0
)

/*
* data class BeneficiarioModel(
    val id: Int? = null,
    val nome: String? = null,
    val contacto: String? = null,
    val referencia: String? = null,
    val familia: String? = null,
    val nacionalidade: String? = null,
    val pedidos: String? = null,
    val notas: String? = null,
    val visitas: Int? = null
) {
    // Necessário para mapeamento do Firestore
    constructor() : this(null, null, null, null, null, null, null, null, null)
}
* */
