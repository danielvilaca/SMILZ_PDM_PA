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
    private lateinit var buttonOrder: FloatingActionButton

    lateinit var toggle : ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializando a referência do botão
        buttonAddBeneficiario = findViewById(R.id.button_add_beneficiario)
        buttonOrder = findViewById(R.id.button_order)

        // Configurando o clique do botão para abrir o diálogo de adicionar beneficiário
        buttonAddBeneficiario.setOnClickListener {
            adicionarBeneficiario()
        }

        buttonOrder.setOnClickListener {
            mostrarDialogoDeFiltro()
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

                R.id.nav_stats -> {
                    val intent = Intent(this, EstatisticasActivity::class.java)
                    startActivity(intent)
                    finish()
                }

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
            /*beneficiarioViewModel.beneficiarios.collect { beneficiarios ->
                recyclerViewBeneficiarios.adapter = BeneficiarioAdapter(
                    beneficiarios,
                    onDetailClick = { beneficiario -> mostrarDetalhes(beneficiario) },
                    onDeleteClick = { beneficiario -> confirmarExclusao(beneficiario) },
                    onAlterarClick = { beneficiario -> alterarBeneficiario(beneficiario) }
                )
            }*/
            beneficiarioViewModel.beneficiarios.collect { beneficiarios ->
                val beneficiariosOrdenados = beneficiarios.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
                recyclerViewBeneficiarios.adapter = BeneficiarioAdapter(
                    beneficiariosOrdenados,
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

    private fun mostrarDialogoDeFiltro() {
        val opcoes = arrayOf("Procurar por nome", "Ordenar por ID", "Ordenar por Nome")
        val dialog = AlertDialog.Builder(this)
            .setTitle("Filtrar/Ordenar Beneficiários")
            .setItems(opcoes) { _, which ->
                when (which) {
                    0 -> procurarPorNome()
                    1 -> ordenarPorId()
                    2 -> ordenarPorNome()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun procurarPorNome() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_procurar_beneficiario, null)
        val editTextNome: EditText = view.findViewById(R.id.edit_text_nome)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Procurar Beneficiário")
            .setView(view)
            .setPositiveButton("Procurar") { _, _ ->
                val nome = editTextNome.text.toString()
                if (nome.isNotEmpty()) {
                    lifecycleScope.launch {
                        val beneficiariosFiltrados = beneficiarioViewModel.beneficiarios.value.filter {
                            it.nome.contains(nome, ignoreCase = true)
                        }
                        atualizarRecyclerView(beneficiariosFiltrados)
                    }
                } else {
                    Toast.makeText(this, "Digite um nome para pesquisar.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun ordenarPorId() {
        lifecycleScope.launch {
            val beneficiariosOrdenados = beneficiarioViewModel.beneficiarios.value.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
            atualizarRecyclerView(beneficiariosOrdenados)
        }
    }

    private fun ordenarPorNome() {
        lifecycleScope.launch {
            val beneficiariosOrdenados = beneficiarioViewModel.beneficiarios.value.sortedBy { it.nome }
            atualizarRecyclerView(beneficiariosOrdenados)
        }
    }

    private fun atualizarRecyclerView(novaLista: List<BeneficiarioModel>) {
        recyclerViewBeneficiarios.adapter = BeneficiarioAdapter(
            novaLista,
            onDetailClick = { beneficiario -> mostrarDetalhes(beneficiario) },
            onDeleteClick = { beneficiario -> confirmarExclusao(beneficiario) },
            onAlterarClick = { beneficiario -> alterarBeneficiario(beneficiario) }
        )
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

        // Gerar um ID único automaticamente
        val id = (System.currentTimeMillis()).toString()  // Usando o timestamp como ID único

        // Configurar o campo edit_id para mostrar o ID gerado, mas não permitir edição
        editId.setText(id)
        editId.isEnabled = false  // Impede a edição do ID

        // Configurar o diálogo
        val dialog = AlertDialog.Builder(this)
            .setTitle("Adicionar Beneficiário")
            .setView(view)
            .setPositiveButton("Adicionar") { _, _ ->
                val nome = editNome.text.toString()
                if (nome.isNotEmpty()) {
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

                // Ignorar as primeiras linhas (cabeçalhos)
                for (row in sheet.drop(2)) {
                    try {
                        Log.d("ExcelDebug", "Lendo linha ${row.rowNum}")

                        // Verificar se a célula do ID está vazia ou inválida
                        val idCell = row.getCell(0)
                        if (idCell == null || (idCell.cellType == CellType.BLANK)) {
                            Log.d("ExcelDebug", "Célula de ID vazia na linha ${row.rowNum}, interrompendo processamento.")
                            break // Interromper o loop
                        }

                        // Log para verificar o tipo de célula ID
                        Log.d("ExcelDebug", "ID: Tipo de célula: ${idCell.cellType}")

                        val id = when (idCell.cellType) {
                            CellType.NUMERIC -> idCell.numericCellValue.toInt().toString()
                            CellType.STRING -> idCell.stringCellValue
                            CellType.FORMULA -> {
                                val evaluator = workbook.creationHelper.createFormulaEvaluator()
                                val cellValue = evaluator.evaluate(idCell)
                                when (cellValue.cellType) {
                                    CellType.NUMERIC -> cellValue.numberValue.toInt().toString()
                                    CellType.STRING -> cellValue.stringValue
                                    else -> {
                                        Log.e("ExcelDebug", "Tipo de fórmula não suportado na linha ${row.rowNum}")
                                        "Erro"
                                    }
                                }
                            }
                            else -> {
                                Log.e("ExcelDebug", "Tipo de célula não suportado na linha ${row.rowNum}")
                                "Erro"
                            }
                        }


                        Log.d("ExcelDebug", "ID processado: $id")

                        // Processar cada coluna com logs detalhados
                        val nome = try {
                            row.getCell(1)?.stringCellValue ?: "Sem Nome"
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Nome' na linha ${row.rowNum}: ${e.message}")
                            "Erro no Nome"
                        }

                        val telemovel = try {
                            row.getCell(2)?.stringCellValue ?: ""
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Telemóvel' na linha ${row.rowNum}: ${e.message}")
                            ""
                        }

                        val referencia = try {
                            row.getCell(3)?.stringCellValue ?: ""
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Referência' na linha ${row.rowNum}: ${e.message}")
                            ""
                        }

                        val familia = try {
                            row.getCell(4)?.numericCellValue?.toInt() ?: 0
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Família' na linha ${row.rowNum}: ${e.message}")
                            0
                        }

                        val nacionalidade = try {
                            row.getCell(5)?.stringCellValue ?: ""
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Nacionalidade' na linha ${row.rowNum}: ${e.message}")
                            ""
                        }

                        val pedidos = try {
                            row.getCell(6)?.stringCellValue ?: ""
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Pedidos' na linha ${row.rowNum}: ${e.message}")
                            ""
                        }

                        val notas = try {
                            row.getCell(7)?.stringCellValue ?: ""
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Notas' na linha ${row.rowNum}: ${e.message}")
                            ""
                        }

                        val visitas = try {
                            row.getCell(8)?.numericCellValue?.toInt() ?: 0
                        } catch (e: Exception) {
                            Log.e("ExcelDebug", "Erro ao processar 'Visitas' na linha ${row.rowNum}: ${e.message}")
                            0
                        }

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

                        Log.d("ExcelDebug", "Beneficiário adicionado: $id")
                    } catch (rowException: Exception) {
                        Log.e("ExcelDebug", "Erro ao processar linha ${row.rowNum}: ${rowException.message}")
                    }
                }

                // Adicionar beneficiários ao Firebase
                lifecycleScope.launch {
                    beneficiarioViewModel.setLoading(true)
                    try {
                        Toast.makeText(this@MainActivity, "A fazer upload...", Toast.LENGTH_SHORT).show()
                        beneficiarioViewModel.addBeneficiarios(beneficiarios)
                        Toast.makeText(this@MainActivity, "Upload concluído!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("ExcelDebug", "Erro ao fazer upload: ${e.message}", e)
                        Toast.makeText(this@MainActivity, "Erro no upload!", Toast.LENGTH_LONG).show()
                    } finally {
                        beneficiarioViewModel.setLoading(false)
                    }
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