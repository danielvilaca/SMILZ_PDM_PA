package com.example.smilz_pdm_pa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.R.id
import com.example.smilz_pdm_pa.R.layout
import com.example.smilz_pdm_pa.R.string
import com.example.smilz_pdm_pa.databinding.ActivityAlterarDadosBinding
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class AlterarBeneficiarioActivity : AppCompatActivity() {

    private lateinit var editId: EditText
    private lateinit var editNome: EditText
    private lateinit var editContacto: EditText
    private lateinit var editReference: EditText
    private lateinit var editFamily: EditText
    private lateinit var editNationality: EditText
    private lateinit var editNotes: EditText
    private lateinit var editRequests: EditText
    private lateinit var editNumVisitas: EditText
    private lateinit var buttonSalvar: Button
    private lateinit var beneficiaryId: String

    private lateinit var binding: ActivityAlterarDadosBinding
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_alterar_dados)
        binding = ActivityAlterarDadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura a Toolbar personalizada
        val toolbar: Toolbar = findViewById(id.toolbar)
        setSupportActionBar(toolbar)  // Define a toolbar como a ActionBar


        val drawerLayout : DrawerLayout = findViewById(id.drawerLayout)
        val navView : NavigationView = findViewById(id.nav_view)

        //toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, string.open, string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                id.nav_home -> {
                    // Redireciona para a MainActivity (Beneficiarios)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                id.nav_beneficiarios -> {
                    // Redireciona para a MainActivity (Beneficiarios)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                id.nav_calendars -> {
                    val intent = Intent(this, EscalaActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_stats -> {
                    val intent = Intent(this, EstatisticasActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                id.nav_logout -> {
                    LoginActivity.logout(this)
                }

            }
            drawerLayout.closeDrawers()

            true
        }

        // Configurar botão de voltar
        binding.buttonVoltar.setOnClickListener { finish() }


        // Obtendo o ID do beneficiário passado pela Intent
        beneficiaryId = intent.getStringExtra("beneficiaryId") ?: return

        // Inicializando os campos de UI
        editId = findViewById(id.edit_id)
        editNome = findViewById(id.edit_nome)
        editContacto = findViewById(id.edit_contacto)
        editReference = findViewById(id.edit_reference)
        editFamily = findViewById(id.edit_family)
        editNationality = findViewById(id.edit_nationality)
        editNotes = findViewById(id.edit_notes)
        editRequests = findViewById(id.edit_requests)
        editNumVisitas = findViewById(id.edit_num_visitas)
        buttonSalvar = findViewById(id.button_salvar)

        // Preenchendo o campo de ID
        editId.setText(beneficiaryId)
        editId.isEnabled = false // Bloquear edição do ID

        // Carregar dados do beneficiário
        carregarDadosBeneficiario()

        // Configuração do botão "Salvar"
        buttonSalvar.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun carregarDadosBeneficiario() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("beneficiarios2").document(beneficiaryId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val beneficiario = document.toObject(BeneficiarioModel::class.java)
                    beneficiario?.let {
                        preencherCampos(it)
                    }
                } else {
                    Toast.makeText(this, "Beneficiário não encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar dados do beneficiário.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun preencherCampos(beneficiario: BeneficiarioModel) {
        editNome.setText(beneficiario.nome)
        editContacto.setText(beneficiario.contacto ?: "")
        editReference.setText(beneficiario.reference ?: "")
        editFamily.setText(beneficiario.family?.toString() ?: "")
        editNationality.setText(beneficiario.nationality ?: "")
        editNotes.setText(beneficiario.notes ?: "")
        editRequests.setText(beneficiario.reference ?: "")
        editNumVisitas.setText(beneficiario.numVisitas?.toString() ?: "")
    }

    private fun salvarAlteracoes() {
        val nome = editNome.text.toString()
        val contacto = editContacto.text.toString()
        val reference = editReference.text.toString()
        val family = editFamily.text.toString().toIntOrNull()
        val nationality = editNationality.text.toString()
        val notes = editNotes.text.toString()
        val requests = editRequests.text.toString()
        val numVisitas = editNumVisitas.text.toString().toIntOrNull()

        if (nome.isBlank()) {
            Toast.makeText(this, "O campo Nome é obrigatório.", Toast.LENGTH_SHORT).show()
            return
        }

        val beneficiarioAtualizado = BeneficiarioModel(
            id = beneficiaryId,
            nome = nome,
            contacto = contacto,
            reference = reference,
            family = family,
            nationality = nationality,
            notes = notes,
            requests = requests,
            numVisitas = numVisitas
        )

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("beneficiarios2").document(beneficiaryId)

        docRef.set(beneficiarioAtualizado)
            .addOnSuccessListener {
                Toast.makeText(this, "Beneficiário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish() // Voltar para a tela anterior
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar beneficiário.", Toast.LENGTH_SHORT).show()
            }


        // Passar os dados todos
        /*
        * lifecycleScope.launch {
        beneficiarioViewModel.updateBeneficiario(beneficiarioId, nome, idade)
        setResult(RESULT_OK) // Indica que a atualização foi concluída com sucesso
        finish() // Fecha a tela de edição
        * */
    }
}
