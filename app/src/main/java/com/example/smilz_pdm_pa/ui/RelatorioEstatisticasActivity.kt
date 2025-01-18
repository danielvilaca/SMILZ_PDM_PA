
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.Relatorio
import com.example.smilz_pdm_pa.ui.ResultadoRelatorioActivity

class RelatorioEstatisticasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estatisticas)

        // Botão de gerar relatório
        val buttonGerarRelatorio: Button = findViewById(R.id.button_gerar_relatorio)
        buttonGerarRelatorio.setOnClickListener {
            // Criar o relatório
            val relatorio = criarRelatorio()

            // Dados -> ResultadoRelatorioActivity
            val intent = Intent(this, ResultadoRelatorioActivity::class.java)
            intent.putExtra("relatorio", relatorio)
            startActivity(intent)
        }
    }

    // Objeto Relatorio
    private fun criarRelatorio(): Relatorio {
        val dadosGrafico = mapOf("Portuguesa" to 50, "Brasileira" to 30, "Angolana" to 20)
        val numeroVisitas = 100
        val nacionalidade = "Todas"

        return Relatorio(
            ano = "2024",
            mes = "Dezembro",
            nacionalidade = nacionalidade,
            numeroVisitas = numeroVisitas,
            dadosGrafico = dadosGrafico
        )
    }
}

