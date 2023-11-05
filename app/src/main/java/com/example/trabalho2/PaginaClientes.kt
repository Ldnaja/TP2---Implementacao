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
import com.example.trabalho2.DAO.ClienteDAO
import com.example.trabalho2.Entity.Cliente

class PaginaClientes : AppCompatActivity() {

    private lateinit var campoNomeCliente: EditText
    private lateinit var campoCPFCliente: EditText
    private lateinit var campoNomeAnimal: EditText
    private lateinit var campoRacaAnimal: EditText

    private lateinit var botaoCadastrarCliente: Button
    private lateinit var botaoVer: Button

    private lateinit var clienteDAO: ClienteDAO

    private lateinit var recyclerView: RecyclerView
    private var adapter: clienteAdaptador? = null
    private var std: ClienteDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_clientes)

        campoNomeCliente = findViewById(R.id.campoNomeCliente)
        campoCPFCliente = findViewById(R.id.campoCPFCliente)
        campoNomeAnimal = findViewById(R.id.campoNomeAnimal)
        campoRacaAnimal = findViewById(R.id.campoRacaAnimal)
        botaoCadastrarCliente = findViewById(R.id.botaoCadastrarCliente)
        botaoVer = findViewById(R.id.botaoVer)
        clienteDAO = ClienteDAO(this)

        initView()

        initRecycler()

        botaoCadastrarCliente.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                try {
                    val nome = campoNomeCliente.text.toString()
                    val cpf = campoCPFCliente.text.toString()
                    val animal = campoNomeAnimal.text.toString()
                    val racaAnimal = campoRacaAnimal.text.toString()

                    if (nome.isNotEmpty() && cpf.isNotEmpty() && animal.isNotEmpty() && racaAnimal.isNotEmpty()) {

                        clienteDAO.open()
                        val clienteExistente = clienteDAO.obterClientePorCpf(cpf)
                        clienteDAO.close()

                        if (clienteExistente == null) {
                            val novoCliente = Cliente()
                            novoCliente.setNome(nome)
                            novoCliente.setCpf(cpf)
                            novoCliente.setAnimal(animal)
                            novoCliente.setRacaAnimal(racaAnimal)

                            clienteDAO.open()
                            val idInserido = clienteDAO.inserirCliente(novoCliente)
                            clienteDAO.close()

                            if (idInserido != -1L) {
                                campoNomeCliente.text.clear()
                                campoCPFCliente.text.clear()
                                campoNomeAnimal.text.clear()
                                campoRacaAnimal.text.clear()
                                exibirMensagem("Cliente cadastrado com sucesso.")
                            } else {
                                exibirMensagem("Erro ao cadastrar o cliente.")
                            }
                        } else {
                            exibirMensagem("Cliente com este CPF já existe.")
                        }
                    } else {
                        exibirMensagem("Preencha todos os campos.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    exibirMensagem("Ocorreu um erro inesperado. Verifique os campos e tente novamente.")
                }
            }
        })

        botaoVer.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                getClientes()
            }
        })

        adapter?.setOnClickDeleteItem {
            deletaCliente(it.toLong())
        }



    }

    private fun deletaCliente(clienteId: Long) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Deseja realmente excluir este cliente?")
        builder.setCancelable(true)
        builder.setPositiveButton("Sim") { dialog, _ ->
            // Excluir o cliente do banco de dados
            clienteDAO.open()
            clienteDAO.deletarClientePorId(clienteId)
            clienteDAO.close()

            // Atualizar a lista de clientes na RecyclerView após a exclusão
            getClientes()

            dialog.dismiss()
        }
        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }


    private fun getClientes() {
        clienteDAO.open()
        val clientes = clienteDAO.obterTodosClientes()
        clienteDAO.close()
        adapter?.addItems(clientes)
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = clienteAdaptador()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerViewClientes)
    }

    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}
