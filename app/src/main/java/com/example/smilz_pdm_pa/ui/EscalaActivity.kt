package com.example.smilz_pdm_pa.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smilz_pdm_pa.R
import com.example.smilz_pdm_pa.viewmodel.VoluntarioViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EscalaActivity : AppCompatActivity() {

    private val voluntarioViewModel: VoluntarioViewModel by viewModels()
    private lateinit var calendarView: CalendarView
    private lateinit var spinnerHoras: Spinner
    private lateinit var buttonConfirmar: Button

    private var dataSelecionada: String? = null
    private val horariosDisponiveis = listOf("09:00 - 11:00", "11:00 - 13:00", "14:00 - 16:00", "16:00 - 18:00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escala)

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