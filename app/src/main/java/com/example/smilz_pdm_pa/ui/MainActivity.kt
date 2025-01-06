package com.example.smilz_pdm_pa.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var beneficiarioViewModel: BeneficiarioViewModel
    private lateinit var recyclerViewBeneficiarios: RecyclerView
    private lateinit var buttonUploadExcel: Button
    private lateinit var buttonAddBeneficiario: FloatingActionButton

    lateinit var toggle : ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializando a referência do botão
        buttonAddBeneficiario = findViewById(R.id.button_add_beneficiario)

        // Configurando o clique do botão para abrir o diálogo de adicionar beneficiário
        buttonAddBeneficiario.setOnClickListener {
            adicionarBeneficiario()
        }


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

                R.id.nav_calendars -> {
                    val intent = Intent(this, EscalaActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_stats -> Toast.makeText(applicationContext, "Clicked Estatisticas", Toast.LENGTH_SHORT).show()

                R.id.nav_logout -> {
                    LoginActivity.logout(this)
                }

            }
            drawerLayout.closeDrawers()

            true
        }


        // Inicializar RecyclerView
        recyclerViewBeneficiarios = findViewById(R.id.recyclerView_beneficiarios)
        recyclerViewBeneficiarios.layoutManager = LinearLayoutManager(this)
        recyclerViewBeneficiarios.setHasFixedSize(true)

        // Botão para adicionar beneficiário
        val buttonAddBeneficiario: FloatingActionButton = findViewById(R.id.button_add_beneficiario)
        buttonAddBeneficiario.setOnClickListener {
            adicionarBeneficiario()
        }

        // Inicializar ViewModel
        beneficiarioViewModel = BeneficiarioViewModel()

        // Observar o StateFlow usando lifecycleScope
        lifecycleScope.launch {
            beneficiarioViewModel.beneficiarios.collect { beneficiarios ->
                recyclerViewBeneficiarios.adapter = BeneficiarioAdapter(
                    beneficiarios,
                    onDetailClick = { beneficiario -> mostrarDetalhes(beneficiario) },
                    onDeleteClick = { beneficiario -> confirmarExclusao(beneficiario) },
                    onAlterarClick = { beneficiario -> alterarBeneficiario(beneficiario) }
                )
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

    private fun confirmarExclusao(beneficiario: BeneficiarioModel) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Excluir Beneficiário")
            .setMessage("Tem certeza de que deseja excluir o beneficiário ${beneficiario.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                lifecycleScope.launch {
                    beneficiarioViewModel.deleteBeneficiario(beneficiario.id)
                    Toast.makeText(
                        this@MainActivity,
                        "Beneficiário excluído com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Não", null)
            .create()

        dialog.show()
    }

    private fun adicionarBeneficiario() {
        // Criar o layout do diálogo
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_adicionar_beneficiario, null)
        val editNome: EditText = view.findViewById(R.id.edit_nome)
        val editId: EditText = view.findViewById(R.id.edit_id)

        // Configurar o diálogo
        val dialog = AlertDialog.Builder(this)
            .setTitle("Adicionar Beneficiário")
            .setView(view)
            .setPositiveButton("Adicionar") { _, _ ->
                val nome = editNome.text.toString()
                val id = editId.text.toString()

                if (nome.isNotEmpty() && id.isNotEmpty()) {
                    val beneficiario = BeneficiarioModel(id = id, nome = nome)
                    lifecycleScope.launch {
                        beneficiarioViewModel.addBeneficiario(beneficiario)
                        Toast.makeText(
                            this@MainActivity,
                            "Beneficiário adicionado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Preencha todos os campos para adicionar o beneficiário.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
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
                for (row in sheet.drop(2)) {
                    try {
                        Log.d("ExcelDebug", "Lendo linha ${row.rowNum}")

                        val id = row.getCell(0)?.let {
                            Log.d("ExcelDebug", "Coluna ID: ${it.cellType}")
                            if (it.cellType == CellType.NUMERIC) {
                                it.numericCellValue.toInt().toString()
                            } else {
                                it.stringCellValue
                            }
                        } ?: "Sem ID"

                        val nome = row.getCell(1)?.let {
                            Log.d("ExcelDebug", "Coluna Nome: ${it.cellType}")
                            it.stringCellValue
                        } ?: "Sem Nome"

                        val telemovel = row.getCell(2)?.let {
                            Log.d("ExcelDebug", "Coluna Telemóvel: ${it.cellType}")
                            it.stringCellValue
                        } ?: ""

                        val referencia = row.getCell(3)?.let {
                            Log.d("ExcelDebug", "Coluna Referência: ${it.cellType}")
                            it.stringCellValue
                        } ?: ""

                        val familia = row.getCell(4)?.let {
                            Log.d("ExcelDebug", "Coluna Família: ${it.cellType}")
                            it.numericCellValue.toInt()
                        } ?: 0

                        val nacionalidade = row.getCell(5)?.let {
                            Log.d("ExcelDebug", "Coluna Nacionalidade: ${it.cellType}")
                            it.stringCellValue
                        } ?: ""

                        val pedidos = row.getCell(6)?.let {
                            Log.d("ExcelDebug", "Coluna Pedidos: ${it.cellType}")
                            it.stringCellValue
                        } ?: ""

                        val notas = row.getCell(7)?.let {
                            Log.d("ExcelDebug", "Coluna Notas: ${it.cellType}")
                            it.stringCellValue
                        } ?: ""

                        val visitas = row.getCell(8)?.let {
                            Log.d("ExcelDebug", "Coluna Visitas: ${it.cellType}")
                            it.numericCellValue.toInt()
                        } ?: 0

                        if (id != "Sem ID" && nome != "Sem Nome") {
                            beneficiarios.add(
                                BeneficiarioModel(
                                    id = id,
                                    nome = nome,
                                    contacto = telemovel,
                                    reference = referencia,
                                    family = familia,
                                    nationality = nacionalidade,
                                    requests = pedidos,
                                    notes = notas,
                                    numVisitas = visitas
                                )
                            )
                        }
                    } catch (cellException: Exception) {
                        Log.e("ExcelDebug", "Erro ao processar linha ${row.rowNum}: ${cellException.message}")
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
            Log.e("ExcelDebug", "Erro geral ao processar o arquivo: ${e.message}", e)
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

    private fun alterarBeneficiario(beneficiario: BeneficiarioModel) {
        val intent = Intent(this, AlterarBeneficiarioActivity::class.java).apply {
            putExtra("beneficiaryId", beneficiario.id)
        }
        startActivity(intent)
    }




}
