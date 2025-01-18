package com.example.smilz_pdm_pa.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.databinding.ActivityLoginBinding
import com.example.smilz_pdm_pa.viewmodel.VoluntarioViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val voluntarioViewModel: VoluntarioViewModel by viewModels() // Instanciar o ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        voluntarioViewModel.isUserLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                // Verifica a MainActivity
                if (isFinishing.not()) {
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()  // Finaliza a LoginActivity
                }
            } else {
                Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.buttonLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            // Verifica se os campos não estão vazios
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Chama a função de login no ViewModel
                voluntarioViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // "Criar conta" -> SignUpActivity
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.textRecoverPassword.setOnClickListener {
            // Verificar (existe acesso direto ao Auth do Firebase)
        }
    }

    companion object {
        fun logout(context: Context) {
            FirebaseAuth.getInstance().signOut()

            // Msg
            Toast.makeText(context, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
