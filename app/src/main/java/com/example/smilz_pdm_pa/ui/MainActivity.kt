// MainActivity.kt
package com.example.smilz_pdm_pa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.smilz_pdm_pa.databinding.ActivityMainBinding
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.example.smilz_pdm_pa.viewmodel.BeneficiarioViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var beneficiarioViewModel: BeneficiarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        beneficiarioViewModel = ViewModelProvider(this).get(BeneficiarioViewModel::class.java)

        // Carregar os beneficiários ao iniciar a activity
        beneficiarioViewModel.loadBeneficiarios()

        // Observar os beneficiários e atualizar a ListView
        beneficiarioViewModel.beneficiarios.observe(this) { beneficiarios ->
            val adapter = BeneficiariosAdapter(this, beneficiarios)
            binding.beneficiariosView.adapter = adapter
        }

        // Exemplo de como adicionar um beneficiário manualmente
        val beneficiario = BeneficiarioModel(
            id = "12345",
            nome = "João Silva",
            contacto = "987654321",
            reference = "Ref123",
            family = 4,
            nationality = "Portuguese",
            notes = "Some notes",
            requests = "None",
            numVisitas = 5
        )
        beneficiarioViewModel.addBeneficiario(beneficiario)
    }
}
