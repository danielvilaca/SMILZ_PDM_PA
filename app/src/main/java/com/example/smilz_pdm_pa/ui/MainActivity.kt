package com.example.smilz_pdm_pa.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.example.smilz_pdm_pa.ui.adapter.BeneficiarioAdapter
import com.example.smilz_pdm_pa.viewmodel.BeneficiarioViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var beneficiarioViewModel: BeneficiarioViewModel
    private lateinit var recyclerViewBeneficiarios: RecyclerView
    private lateinit var buttonUploadExcel: Button

    lateinit var toggle : ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Configura a Toolbar personalizada
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)  // Define a toolbar como a ActionBar


        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        //toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                //R.id.nav_home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                //R.id.nav_beneficiarios -> Toast.makeText(applicationContext, "Clicked Beneficiarios", Toast.LENGTH_SHORT).show()

                R.id.nav_home -> {
                    // Redireciona para a MainActivity (Beneficiarios)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_beneficiarios -> {
                    // Redireciona para a MainActivity (Beneficiarios)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_calendars -> Toast.makeText(applicationContext, "Clicked Escalas", Toast.LENGTH_SHORT).show()
                R.id.nav_stats -> Toast.makeText(applicationContext, "Clicked Estatisticas", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> Toast.makeText(applicationContext, "Clicked Logout", Toast.LENGTH_SHORT).show()

            }
            drawerLayout.closeDrawers()

            true
        }


        // Inicializar RecyclerView
        recyclerViewBeneficiarios = findViewById(R.id.recyclerView_beneficiarios)
        recyclerViewBeneficiarios.layoutManager = LinearLayoutManager(this)
        recyclerViewBeneficiarios.setHasFixedSize(true)

        // Inicializar ViewModel
        beneficiarioViewModel = BeneficiarioViewModel()

        // Observar o StateFlow usando lifecycleScope
        lifecycleScope.launch {
            beneficiarioViewModel.beneficiarios.collect { beneficiarios ->
                recyclerViewBeneficiarios.adapter = BeneficiarioAdapter(beneficiarios) { beneficiario ->
                    mostrarDetalhes(beneficiario)
                }
            }
        }

        // Carregar os beneficiários da base de dados
        beneficiarioViewModel.fetchBeneficiarios()

        // Configurar botão para upload do Excel
        buttonUploadExcel = findViewById(R.id.button_upload_excel)
        buttonUploadExcel.setOnClickListener {
            abrirSeletorDeArquivo()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }


        return super.onOptionsItemSelected(item)
    }

    private fun abrirSeletorDeArquivo() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        }
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        processarArquivoExcel(inputStream)
                    }
                }
            } else {
                Toast.makeText(this, "Seleção de arquivo cancelada.", Toast.LENGTH_SHORT).show()
            }
        }



    private fun processarArquivoExcel(inputStream: InputStream) {
        try {
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheet("Beneficiários-novo")

            if (sheet != null) {
                val beneficiarios = mutableListOf<BeneficiarioModel>()

                // Ignorar a primeira linha (cabeçalhos)
                for (row in sheet.drop(1)) {
                    val id = row.getCell(0)?.numericCellValue?.toInt()?.toString() ?: "Sem ID"
                    val nome = row.getCell(1)?.stringCellValue ?: "Sem Nome"

                    if (id != "Sem ID" && nome != "Sem Nome") {
                        beneficiarios.add(BeneficiarioModel(id = id, nome = nome))
                    }
                }

                // Adicionar beneficiários ao Firebase
                lifecycleScope.launch {
                    beneficiarios.forEach { beneficiario ->
                        beneficiarioViewModel.addBeneficiario(beneficiario)
                    }
                    Toast.makeText(this@MainActivity, "Upload concluído!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Planilha 'Beneficiários-novo' não encontrada.", Toast.LENGTH_SHORT).show()
            }

            workbook.close()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao processar o arquivo: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarDetalhes(beneficiario: BeneficiarioModel) {
        // Exemplo: Abrir uma nova Activity para detalhes
        val intent = Intent(this, DetalhesBeneficiarioActivity::class.java).apply {
            putExtra("beneficiario_id", beneficiario.id)
        }
        startActivity(intent)
    }



}
