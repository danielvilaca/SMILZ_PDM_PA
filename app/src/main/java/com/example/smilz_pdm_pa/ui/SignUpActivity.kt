package com.example.smilz_pdm_pa.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smilz_pdm_pa.databinding.ActivitySignUpBinding
import com.example.smilz_pdm_pa.model.VoluntarioModel
import com.example.smilz_pdm_pa.viewmodel.VoluntarioViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val voluntarioViewModel: VoluntarioViewModel by viewModels() // Instancia o ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Clique no botão de registo
        binding.buttonSignup.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            val confirmPassword = binding.editConfirmPassword.text.toString()
            val nome = binding.editName.text.toString()
            val role = "voluntario"  // Pode ser fixo ou dinâmico, dependendo do seu design

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
    }
}
