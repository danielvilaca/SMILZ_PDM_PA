package com.example.smilz_pdm_pa.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.BeneficiarioModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_dados)

        // Obtendo o ID do beneficiário passado pela Intent
        beneficiaryId = intent.getStringExtra("beneficiaryId") ?: return

        // Inicializando os campos de UI
        editId = findViewById(R.id.edit_id)
        editNome = findViewById(R.id.edit_nome)
        editContacto = findViewById(R.id.edit_contacto)
        editReference = findViewById(R.id.edit_reference)
        editFamily = findViewById(R.id.edit_family)
        editNationality = findViewById(R.id.edit_nationality)
        editNotes = findViewById(R.id.edit_notes)
        editRequests = findViewById(R.id.edit_requests)
        editNumVisitas = findViewById(R.id.edit_num_visitas)
        buttonSalvar = findViewById(R.id.button_salvar)

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
        val docRef = db.collection("beneficiarios").document(beneficiaryId)

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
        editRequests.setText(beneficiario.requests ?: "")
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
        val docRef = db.collection("beneficiarios").document(beneficiaryId)

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
