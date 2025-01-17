package com.example.smilz_pdm_pa.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.databinding.ActivityResultadoRelatorioBinding
import com.example.smilz_pdm_pa.model.Relatorio
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ResultadoRelatorioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultadoRelatorioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_relatorio)

        binding = ActivityResultadoRelatorioBinding.inflate(layoutInflater)

        // Obtém o relatório passado pela Intent
        val relatorio = intent.getSerializableExtra("relatorio") as Relatorio

        // Configura o gráfico
        val pieChart = findViewById<PieChart>(R.id.pie_chart)
        val pieEntries = relatorio.dadosGrafico.map { PieEntry(it.value.toFloat(), it.key) }
        val dataSet = PieDataSet(pieEntries, "Nacionalidades")
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate() // Atualiza o gráfico

        // Configura o número de visitas
        val textNumeroVisitas: TextView = findViewById(R.id.text_numero_visitas)
        textNumeroVisitas.text = "Número total de visitas: ${relatorio.numeroVisitas}"

        // Configura o botão para exportar
        val buttonExportar: Button = findViewById(R.id.button_exportar)
        buttonExportar.setOnClickListener {
            pieChart.saveToGallery("relatorio_${relatorio.ano}_${relatorio.mes}", 85)
        }

        // Configurar botão de voltar
        binding.buttonVoltar.setOnClickListener { finish() }
    }
}
