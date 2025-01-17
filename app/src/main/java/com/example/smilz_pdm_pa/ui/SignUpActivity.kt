package com.example.smilz_pdm_pa.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.smilz_pdm_pa.databinding.ActivitySignUpBinding
import com.example.smilz_pdm_pa.model.VoluntarioModel
import com.example.smilz_pdm_pa.viewmodel.VoluntarioViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var voluntarioViewModel: VoluntarioViewModel //by viewModels() | Instancia o ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o ViewModel
        voluntarioViewModel = ViewModelProvider(this).get(VoluntarioViewModel::class.java)

        // Preencher o Spinner com as funções fixas
        val roles = arrayOf("Admin", "Vol", "VolL", "PresJunta")
        val roleAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, roles)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRole.adapter = roleAdapter

        // Observa o estado de registo
        voluntarioViewModel.isUserRegistered.observe(this) { isRegistered ->
            if (isRegistered) {
                Toast.makeText(this, "Registo bem-sucedido! Faça login.", Toast.LENGTH_SHORT).show()
                // Redirecionar para a LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Finaliza a SignUpActivity
            } else {
                Toast.makeText(this, "Falha ao registar o utilizador. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }

        // Clique no botão de registo
        binding.buttonSignup.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            val confirmPassword = binding.editConfirmPassword.text.toString()
            val nome = binding.editName.text.toString()
            val role = binding.spinnerRole.selectedItem.toString()  // Obtém o valor selecionado no Spinner

            // Verifica se todos os campos estão preenchidos
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && nome.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Cria o modelo do voluntário
                    val voluntario = VoluntarioModel(
                        email = email,
                        senha = password,
                        nome = nome,
                        role = role,
                        dataRegisto = System.currentTimeMillis().toString()
                    )

                    // Chama a função de registo no ViewModel
                    voluntarioViewModel.registerUser(voluntario)
                } else {
                    Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar botão de voltar
        binding.buttonVoltar.setOnClickListener { finish() }
    }
}
