
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

        // Inicializa o botão de geração de relatório
        val buttonGerarRelatorio: Button = findViewById(R.id.button_gerar_relatorio)
        buttonGerarRelatorio.setOnClickListener {
            // Cria o relatório com os dados selecionados
            val relatorio = criarRelatorio()

            // Passa os dados para a ResultadoRelatorioActivity
            val intent = Intent(this, ResultadoRelatorioActivity::class.java)
            intent.putExtra("relatorio", relatorio)
            startActivity(intent)
        }
    }

    // Função que cria o objeto Relatorio com os dados selecionados
    private fun criarRelatorio(): Relatorio {
        // Exemplo de dados. Você pode modificar com base nos dados reais da UI
        val dadosGrafico = mapOf("Portuguesa" to 50, "Brasileira" to 30, "Angolana" to 20)
        val numeroVisitas = 100 // Isso pode vir de algum cálculo ou entrada do usuário
        val nacionalidade = "Todas" // Supondo que seja algo como "Todas" ou um valor selecionado pelo usuário

        return Relatorio(
            ano = "2024",               // Passando o ano como String
            mes = "Dezembro",           // Passando o mês como String
            nacionalidade = nacionalidade, // Passando a nacionalidade como String
            numeroVisitas = numeroVisitas, // Passando o número de visitas como Int
            dadosGrafico = dadosGrafico  // Passando os dados do gráfico como Map<String, Int>
        )
    }
}

