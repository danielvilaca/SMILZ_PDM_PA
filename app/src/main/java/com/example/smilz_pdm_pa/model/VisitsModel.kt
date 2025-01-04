package com.example.smilz_pdm_pa.model

data class VisitsModel(
    val id : Int = 0,
    //val BeneficiarioModel.id
    val Year : Int = 0,
    val Weekday : String = "",
    val visited : Boolean
)
