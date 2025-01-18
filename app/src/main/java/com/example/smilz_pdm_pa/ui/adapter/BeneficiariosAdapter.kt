package com.example.smilz_pdm_pa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.BeneficiarioModel

class BeneficiarioAdapter(
    private val beneficiarios: List<BeneficiarioModel>,
    private val onDetailClick: (BeneficiarioModel) -> Unit,
    private val onDeleteClick: (BeneficiarioModel) -> Unit,
    private val onAlterarClick: (BeneficiarioModel) -> Unit
) : RecyclerView.Adapter<BeneficiarioAdapter.BeneficiarioViewHolder>() {

    // Referências layout
    class BeneficiarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNome: TextView = itemView.findViewById(R.id.text_nome)
        val textId: TextView = itemView.findViewById(R.id.text_id)
        val buttonVerDetalhes: Button = itemView.findViewById(R.id.button_ver_detalhes)
        val buttonExcluir: Button = itemView.findViewById(R.id.button_excluir)
        val buttonAlterar: Button = itemView.findViewById(R.id.button_alterar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_beneficiario, parent, false)
        return BeneficiarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeneficiarioViewHolder, position: Int) {
        val beneficiario = beneficiarios[position]
        holder.textNome.text = beneficiario.nome
        holder.textId.text = beneficiario.id ?: "Sem ID"
        holder.buttonVerDetalhes.setOnClickListener { onDetailClick(beneficiario) }
        holder.buttonExcluir.setOnClickListener { onDeleteClick(beneficiario) }
        holder.buttonAlterar.setOnClickListener { onAlterarClick(beneficiario) }
    }

    override fun getItemCount(): Int = beneficiarios.size
}
