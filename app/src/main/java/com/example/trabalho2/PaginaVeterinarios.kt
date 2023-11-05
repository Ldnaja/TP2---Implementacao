package com.example.trabalho2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho2.DAO.VeterinarioDAO
import com.example.trabalho2.Entity.Veterinario

class PaginaVeterinarios : AppCompatActivity() {

    private lateinit var campoNomeVeterinario: EditText
    private lateinit var campoEspecializacoVeterinario: EditText
    private lateinit var campoCRMVVeterinario: EditText

    private lateinit var botaoCadastrarVeterinario: Button
    private lateinit var botaoVerFuncionarios: Button

    private lateinit var veterinarioDAO: VeterinarioDAO

    private lateinit var recyclerView: RecyclerView
    private var adapter: veterinarioAdaptador? = null
    private var std: VeterinarioDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_veterinarios)

        campoNomeVeterinario = findViewById(R.id.campoNomeVeterinario)
        campoEspecializacoVeterinario = findViewById(R.id.campoEspecializacoVeterinario)
        campoCRMVVeterinario = findViewById(R.id.campoCRMVVeterinario)
        botaoCadastrarVeterinario = findViewById(R.id.botaoCadastrarVeterinario)
        botaoVerFuncionarios = findViewById(R.id.botaoVerFuncionarios)
        veterinarioDAO = VeterinarioDAO(this)

        initView()
        initRecycler()

        botaoCadastrarVeterinario.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                try {
                    val nome = campoNomeVeterinario.text.toString()
                    val especializacao = campoEspecializacoVeterinario.text.toString()
                    val crmv = campoCRMVVeterinario.text.toString()

                    if (nome.isNotEmpty() && especializacao.isNotEmpty() && crmv.isNotEmpty()) {

                        veterinarioDAO.open()
                        val veterinarioExistente = veterinarioDAO.obterVeterinarioPorCRMV(crmv)
                        veterinarioDAO.close()

                        if (veterinarioExistente == null) {
                            val novoVeterinario = Veterinario()
                            novoVeterinario.setNome(nome)
                            novoVeterinario.setEspecializacao(especializacao)
                            novoVeterinario.setCrmv(crmv)

                            veterinarioDAO.open()
                            val idInserido = veterinarioDAO.inserirVeterinario(novoVeterinario)
                            veterinarioDAO.close()

                            if (idInserido != -1L) {
                                campoNomeVeterinario.text.clear()
                                campoEspecializacoVeterinario.text.clear()
                                campoCRMVVeterinario.text.clear()
                                exibirMensagem("Veterinário cadastrado com sucesso!")
                            } else {
                                exibirMensagem("Erro ao cadastrar veterinário.")
                            }
                        } else {
                            exibirMensagem("Veterinário já cadastrado.")
                        }
                    } else {
                        exibirMensagem("Preencha todos os campos.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    exibirMensagem("Outro Erro ao cadastrar veterinário.")
                }
            }
        })

        botaoVerFuncionarios.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                getVeterinarios()
            }
        })

        adapter?.setOnClickDeleteItem{
            deletaVeterinario(it.toLong())
        }
    }

    private fun deletaVeterinario(veterinarioId: Long){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Deseja realmente excluir este veterinário?")
        builder.setPositiveButton("Sim"){dialog, which ->
            veterinarioDAO.open()
            veterinarioDAO.deletarVeterinarioPorId(veterinarioId)
            veterinarioDAO.close()

            getVeterinarios()

            dialog.dismiss()
        }
        builder.setNegativeButton("Não"){dialog, which ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun getVeterinarios(){
        veterinarioDAO.open()
        val veterinarios = veterinarioDAO.obterTodosVeterinarios()
        veterinarioDAO.close()
        adapter?.addItems(veterinarios)
    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = veterinarioAdaptador()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        recyclerView = findViewById(R.id.recyclerViewVeterinario)
    }

    private fun exibirMensagem(mensagem: String){
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()
    }
}