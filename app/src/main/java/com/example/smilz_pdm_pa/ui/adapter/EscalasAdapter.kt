import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.Escala

class EscalasAdapter(private var escalas: List<Escala>) : RecyclerView.Adapter<EscalasAdapter.EscalaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EscalaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_escala, parent, false)
        return EscalaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EscalaViewHolder, position: Int) {
        val escala = escalas[position]
        holder.bind(escala)
    }

    override fun getItemCount(): Int = escalas.size

    class EscalaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dataTextView: TextView = itemView.findViewById(R.id.textViewData)
        private val horarioTextView: TextView = itemView.findViewById(R.id.textViewHorario)
        private val nomeTextView: TextView = itemView.findViewById(R.id.textViewNome)
        private val roleTextView: TextView = itemView.findViewById(R.id.textViewRole)

        fun bind(escala: Escala) {
            dataTextView.text = escala.data
            horarioTextView.text = escala.horario
            nomeTextView.text = escala.voluntarioNome
            roleTextView.text = escala.voluntarioId // Pode ser o nome ou o ID, dependendo da tua necessidade
        }
    }

    fun atualizarEscalas(novasEscalas: List<Escala>) {
        this.escalas = novasEscalas
        notifyDataSetChanged()
    }
}


