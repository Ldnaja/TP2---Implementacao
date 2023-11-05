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
import com.example.trabalho2.DAO.FuncionarioDAO
import com.example.trabalho2.Entity.Funcionario

class PaginaEditarFuncionarios : AppCompatActivity() {

    private lateinit var campoLoginPaginaFuncionario: EditText
    private lateinit var campoSenhaPaginaFuncionario: EditText

    private lateinit var botaoCadastrarFuncionarioPagina: Button
    private lateinit var botaoVerFuncionariosPagina: Button
    private lateinit var botaoAtualizarFuncionarioPagina: Button

    private lateinit var funcionarioDAO: FuncionarioDAO

    private lateinit var recyclerView: RecyclerView
    private var adapter: funcionarioAdaptador? = null
    private var std: Funcionario? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_editar_funcionarios)

        campoLoginPaginaFuncionario = findViewById(R.id.campoLoginPaginaFuncionario)
        campoSenhaPaginaFuncionario = findViewById(R.id.campoSenhaPaginaFuncionario)
        botaoCadastrarFuncionarioPagina = findViewById(R.id.botaoCadastrarFuncionarioPagina)
        botaoVerFuncionariosPagina = findViewById(R.id.botaoVerFuncionariosPagina)
        botaoAtualizarFuncionarioPagina = findViewById(R.id.botaoAtualizarFuncionarioPagina)
        funcionarioDAO = FuncionarioDAO(this)

        initView()
        initRecycler()

        botaoCadastrarFuncionarioPagina.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View){
                try {
                    val login = campoLoginPaginaFuncionario.text.toString()
                    val senha = campoSenhaPaginaFuncionario.text.toString()

                    if (login.isNotEmpty() && senha.isNotEmpty()) {

                        funcionarioDAO.open()
                        val funcionarioExistente = funcionarioDAO.obterFuncionarioPorLogin(login)
                        funcionarioDAO.close()

                        if (funcionarioExistente == null) {
                            val novoFuncionario = Funcionario()
                            novoFuncionario.setLogin(login)
                            novoFuncionario.setSenha(senha)

                            funcionarioDAO.open()
                            val idInserido = funcionarioDAO.inserirFuncionario(novoFuncionario)
                            funcionarioDAO.close()

                            if(idInserido != -1L){
                                campoLoginPaginaFuncionario.text.clear()
                                campoSenhaPaginaFuncionario.text.clear()
                                exibirMensagem("Funcionário cadastrado com sucesso")
                            } else {
                                exibirMensagem("Erro ao inserir funcionário")
                            }
                        } else {
                            exibirMensagem("Funcionário já cadastrado")
                        }
                    } else {
                        exibirMensagem("Preencha todos os campos")
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                    exibirMensagem("Erro ao cadastrar funcionário")
                }
            }
        })

        botaoVerFuncionariosPagina.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View){
                getFuncionarios()
            }
        })

        botaoAtualizarFuncionarioPagina.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                try {
                    val senha = campoSenhaPaginaFuncionario.text.toString()

                    std?.let { funcionario ->
                        if (senha.isNotEmpty()) {
                            funcionarioDAO.open()
                            val rowsAffected = funcionarioDAO.atualizarSenhaFuncionario(funcionario.idFuncionario.toLong(), senha)
                            funcionarioDAO.close()

                            if (rowsAffected > 0) {
                                exibirMensagem("Senha do funcionário atualizada com sucesso")
                                campoLoginPaginaFuncionario.text = Editable.Factory.getInstance().newEditable(funcionario.login)
                                campoSenhaPaginaFuncionario.text = Editable.Factory.getInstance().newEditable("")  // Limpar o campo de senha
                                getFuncionarios()
                            } else {
                                exibirMensagem("Erro ao atualizar senha do funcionário")
                            }
                        } else {
                            exibirMensagem("Preencha a senha e selecione um funcionário para atualizar")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    exibirMensagem("Erro ao atualizar senha do funcionário")
                }
            }
        })


        adapter?.setOnClickDeleteItem{
            deletarFuncionario(it.toLong())
        }

        adapter?.setOnClickItem { funcionarioId ->
            val funcionario = adapter?.getFuncionarioById(funcionarioId)
            funcionario?.let {
                Toast.makeText(this, "Funcionário ${it.login}", Toast.LENGTH_SHORT).show()
                campoLoginPaginaFuncionario.text = Editable.Factory.getInstance().newEditable(it.login)
                campoSenhaPaginaFuncionario.text = Editable.Factory.getInstance().newEditable("")  // Limpar o campo de senha
                std = it
            }
        }


    }


    private fun deletarFuncionario(funcioarioId: Long){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Deseja remover o funcionário?")
        builder.setPositiveButton("Sim"){dialog, which ->
            funcionarioDAO.open()
            funcionarioDAO.deletarFuncionarioPorId(funcioarioId)
            funcionarioDAO.close()

            getFuncionarios()

            dialog.dismiss()
        }
        builder.setNegativeButton("Não"){dialog, which ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun getFuncionarios(){
        funcionarioDAO.open()
        val funcionarios = funcionarioDAO.obterTodosFuncionarios()
        funcionarioDAO.close()
        adapter?.addItems(funcionarios)
    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = funcionarioAdaptador()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        recyclerView = findViewById(R.id.recyclerViewFuncionario)
    }

    private fun exibirMensagem(mensagem: String){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}