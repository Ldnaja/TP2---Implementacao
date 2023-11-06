package com.example.trabalho2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TelaGerenciamento : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_gerenciamento)

        val botaoCadastrarCliente = findViewById<Button>(R.id.buttonCadastrarCliente)
        val botaoCadastraVeterinario = findViewById<Button>(R.id.buttonCadrastarVeterinario)
        val botaoEditarFuncionario = findViewById<Button>(R.id.buttonEditarFuncionarios)
        val botaoConsulta = findViewById<Button>(R.id.buttonConsulta)
        val botaoDesmarcarConsulta = findViewById<Button>(R.id.buttonDesmarcarConsulta)

        botaoCadastrarCliente.setOnClickListener {
            val intent = Intent(this, PaginaClientes::class.java)
            startActivity(intent)
        }

        botaoCadastraVeterinario.setOnClickListener {
            val intent = Intent(this, PaginaVeterinarios::class.java)
            startActivity(intent)
        }

        botaoEditarFuncionario.setOnClickListener {
            val intent = Intent(this, PaginaEditarFuncionarios::class.java)
            startActivity(intent)
        }

        botaoConsulta.setOnClickListener {
            val intent = Intent(this, MarcarConsulta::class.java)
            startActivity(intent)
        }

        botaoDesmarcarConsulta.setOnClickListener {
            val intent = Intent(this, DesmarcarConsulta::class.java)
            startActivity(intent)
        }
    }
}