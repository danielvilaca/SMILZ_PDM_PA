package com.example.smilz_pdm_pa.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.example.smilz_pdm_pa.repository.BeneficiarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalhesBeneficiarioActivity : AppCompatActivity() {

    private lateinit var textId: TextView
    private lateinit var textNome: TextView
    private lateinit var textContacto: TextView
    private lateinit var textReference: TextView
    private lateinit var buttonVoltar: Button

    private val repository = BeneficiarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_beneficiario)

        // Inicializar Views
        textId = findViewById(R.id.text_id)
        textNome = findViewById(R.id.text_nome)
        textContacto = findViewById(R.id.text_contacto)
        textReference = findViewById(R.id.text_reference)
        buttonVoltar = findViewById(R.id.button_voltar)

        // Configurar botão de voltar
        buttonVoltar.setOnClickListener {
            finish()
        }

        // Obter o ID do beneficiário a partir do Intent
        val beneficiarioId = intent.getStringExtra("beneficiario_id")
        if (beneficiarioId != null) {
            carregarDetalhes(beneficiarioId)
        } else {
            Toast.makeText(this, "Erro ao carregar o beneficiário.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun carregarDetalhes(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val beneficiario = repository.getBeneficiarioById(id)
                withContext(Dispatchers.Main) {
                    if (beneficiario != null) {
                        mostrarDetalhes(beneficiario)
                    } else {
                        Toast.makeText(this@DetalhesBeneficiarioActivity, "Beneficiário não encontrado.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetalhesBeneficiarioActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun mostrarDetalhes(beneficiario: BeneficiarioModel) {
        textId.text = "ID: ${beneficiario.id}"
        textNome.text = "Nome: ${beneficiario.nome}"
        textContacto.text = "Contacto: ${beneficiario.contacto ?: "N/A"}"
        textReference.text = "Referência: ${beneficiario.reference ?: "N/A"}"
    }
}
