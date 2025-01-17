package com.example.smilz_pdm_pa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.Relatorio

class RelatoriosAdapter(
    private val relatorios: List<Relatorio>,
    private val onVerClick: (Relatorio) -> Unit
) : RecyclerView.Adapter<RelatoriosAdapter.RelatorioViewHolder>() {

    class RelatorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNome: TextView = itemView.findViewById(R.id.text_nome)
        val buttonVerDetalhes: Button = itemView.findViewById(R.id.button_ver_detalhes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatorioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estatistica, parent, false)
        return RelatorioViewHolder(view)
    }

    override fun onBindViewHolder(holder: RelatorioViewHolder, position: Int) {
        val relatorio = relatorios[position]
        holder.textNome.text = "${relatorio.ano}-${relatorio.mes}"

        holder.buttonVerDetalhes.setOnClickListener {
            onVerClick(relatorio)
        }
    }

    override fun getItemCount(): Int = relatorios.size
}
