package com.example.smilz_pdm_pa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.viewmodel.VoluntarioViewModel
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EscalaActivity : AppCompatActivity() {

    private val voluntarioViewModel: VoluntarioViewModel by viewModels()
    private lateinit var calendarView: CalendarView
    private lateinit var spinnerHoras: Spinner
    private lateinit var buttonConfirmar: Button

    lateinit var toggle : ActionBarDrawerToggle

    private var dataSelecionada: String? = null
    private val horariosDisponiveis = listOf("09:00 - 11:00", "11:00 - 13:00", "14:00 - 16:00", "16:00 - 18:00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escala)


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

                R.id.nav_calendars -> Toast.makeText(applicationContext, "Clicked Escalas", Toast.LENGTH_SHORT).show()
                R.id.nav_stats -> Toast.makeText(applicationContext, "Clicked Estatisticas", Toast.LENGTH_SHORT).show()

                R.id.nav_logout -> {
                    LoginActivity.logout(this)
                }

            }
            drawerLayout.closeDrawers()

            true
        }

        // Referências aos componentes do layout
        calendarView = findViewById(R.id.calendarView)
        spinnerHoras = findViewById(R.id.spinnerHoras)
        buttonConfirmar = findViewById(R.id.buttonConfirmar)

        // Configurar spinner de horários
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, horariosDisponiveis)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHoras.adapter = adapter

        // Configurar CalendarView para selecionar apenas sábados
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                dataSelecionada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                Toast.makeText(this, "Data selecionada: $dataSelecionada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, selecione um sábado.", Toast.LENGTH_SHORT).show()
                dataSelecionada = null
            }
        }

        // Ação do botão confirmar
        buttonConfirmar.setOnClickListener {
            if (dataSelecionada != null) {
                val horarioSelecionado = spinnerHoras.selectedItem.toString()
                confirmarEscala(dataSelecionada!!, horarioSelecionado)
            } else {
                Toast.makeText(this, "Selecione um sábado e um horário.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmarEscala(data: String, horario: String) {
        // Chamada ao ViewModel para salvar a escala
        voluntarioViewModel.registrarEscala(data, horario)
        Toast.makeText(this, "Escala confirmada para $data às $horario.", Toast.LENGTH_SHORT).show()
    }
}