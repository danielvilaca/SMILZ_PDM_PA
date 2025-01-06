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

        // Bloquear edição do ID (não alterável)
        editId.setText(beneficiaryId)

        // Preencher os campos com os dados do beneficiário
        getBeneficiaryData(beneficiaryId)

        // Configuração do botão "Salvar"
        buttonSalvar.setOnClickListener {
            val updatedBeneficiary = BeneficiarioModel(
                id = editId.text.toString(),
                nome = editNome.text.toString(),
                contacto = editContacto.text.toString(),
                reference = editReference.text.toString(),
                family = editFamily.text.toString().toIntOrNull(),
                nationality = editNationality.text.toString(),
                notes = editNotes.text.toString(),
                requests = editRequests.text.toString(),
                numVisitas = editNumVisitas.text.toString().toIntOrNull()
            )
            updateBeneficiaryData(updatedBeneficiary)
        }
    }

    private fun getBeneficiaryData(beneficiaryId: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("beneficiarios").document(beneficiaryId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val beneficiary = document.toObject(BeneficiarioModel::class.java)
                    beneficiary?.let {
                        // Preencher os campos com os dados
                        editNome.setText(it.nome)
                        editContacto.setText(it.contacto)
                        editReference.setText(it.reference)
                        editFamily.setText(it.family?.toString())
                        editNationality.setText(it.nationality)
                        editNotes.setText(it.notes)
                        editRequests.setText(it.requests)
                        editNumVisitas.setText(it.numVisitas?.toString())
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar dados", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateBeneficiaryData(updatedBeneficiary: BeneficiarioModel) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("beneficiarios").document(updatedBeneficiary.id)

        docRef.set(updatedBeneficiary)
            .addOnSuccessListener {
                Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                finish() // Volta para a MainActivity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao atualizar os dados", Toast.LENGTH_SHORT).show()
            }
    }


}
