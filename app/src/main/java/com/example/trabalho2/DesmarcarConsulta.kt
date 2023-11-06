package com.example.trabalho2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho2.DAO.VeterinarioDAO
import com.example.trabalho2.DAO.ClienteDAO
import com.example.trabalho2.DAO.ConsultaDAO
import com.example.trabalho2.Entity.Consulta
import com.example.trabalho2.Entity.Cliente
import com.example.trabalho2.Entity.Veterinario

class DesmarcarConsulta : AppCompatActivity() {

    private lateinit var botaoMostrarConsultas: Button

    private lateinit var consultaDAO: ConsultaDAO
    private lateinit var clienteDAO: ClienteDAO
    private lateinit var veterinarioDAO: VeterinarioDAO

    private lateinit var recyclerView: RecyclerView
    private var adapter: desmarcarConsultaAdaptador? = null
    private var std: Consulta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desmarcar_consulta)

        botaoMostrarConsultas = findViewById(R.id.botaoMostrarConsultas)

        consultaDAO = ConsultaDAO(this)
        clienteDAO = ClienteDAO(this)
        veterinarioDAO = VeterinarioDAO(this)

        initView()
        initRecycler()

        botaoMostrarConsultas.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                consultaDAO.open()
                val consultas = consultaDAO.obterTodasConsultas()
                consultaDAO.close()
                adapter?.addItems(consultas)
            }
        })

        adapter?.setOnClickDeleteItem{
            deletarConsulta(it.toLong())
        }
    }

    private fun deletarConsulta(consultaId: Long) {
        consultaDAO.open()

        // Verifique se a consulta existe
        val consulta = consultaDAO.obterConsulta(consultaId)
        if (consulta != null) {
            consultaDAO.excluirConsulta(consultaId)
            exibirMensagem("Consulta desmarcada com sucesso!")
        } else {
            exibirMensagem("Consulta não encontrada")
        }

        consultaDAO.close()
    }


    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = desmarcarConsultaAdaptador()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        recyclerView = findViewById(R.id.recyclerViewDesmarcarConsulta)
    }

    private fun exibirMensagem(mensagem: String){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}