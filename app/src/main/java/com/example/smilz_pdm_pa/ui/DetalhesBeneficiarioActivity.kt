package com.example.smilz_pdm_pa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.databinding.ActivityDetalhesBeneficiarioBinding
import com.example.smilz_pdm_pa.model.BeneficiarioModel
import com.example.smilz_pdm_pa.repository.BeneficiarioRepository
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalhesBeneficiarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesBeneficiarioBinding
    private val repository = BeneficiarioRepository()

    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesBeneficiarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        // Configurar botão de voltar
        binding.buttonVoltar.setOnClickListener { finish() }

        // Obter o ID do beneficiário do Intent
        val beneficiarioId = intent.getStringExtra("beneficiario_id")
        if (!beneficiarioId.isNullOrEmpty()) {
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
        binding.apply {
            textId.text = "ID: ${beneficiario.id}"
            textNome.text = "Nome: ${beneficiario.nome}"
            textContacto.text = "Contacto: ${beneficiario.contacto ?: "N/A"}"
            textReference.text = "Referência: ${beneficiario.reference ?: "N/A"}"
            textFamily.text = "Família: ${beneficiario.family ?: "N/A"}"
            textNationality.text = "Nacionalidade: ${beneficiario.nationality ?: "N/A"}"
            textRequests.text = "Pedidos: ${beneficiario.requests ?: "N/A"}"
            textNotes.text = "Notas: ${beneficiario.notes ?: "N/A"}"

        }
    }
}
