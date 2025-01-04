// BeneficiariosAdapter.kt
package com.example.smilz_pdm_pa.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.BeneficiarioModel

class BeneficiariosAdapter(
    private val context: MainActivity,
    private val beneficiarios: List<BeneficiarioModel>
) : ArrayAdapter<BeneficiarioModel>(context, R.layout.item_beneficiario, beneficiarios) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val beneficiario = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_beneficiario, parent, false)

        val nomeTextView = view.findViewById<TextView>(R.id.text_nome)
        val idTextView = view.findViewById<TextView>(R.id.text_id)

        nomeTextView.text = beneficiario?.nome
        idTextView.text = beneficiario?.id

        return view
    }
}
