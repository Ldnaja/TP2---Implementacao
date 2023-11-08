package com.example.trabalho2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.trabalho2.DAO.FuncionarioDAO
import com.example.trabalho2.Entity.Funcionario

class PaginaCadastrarFuncionario : AppCompatActivity() {

    private lateinit var campoLogin: EditText
    private lateinit var campoSenha: EditText
    private lateinit var buttonSalvar: Button

    private lateinit var funcionarioDAO: FuncionarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_cadastrar_funcionario)

        campoLogin = findViewById(R.id.CampoLogin)
        campoSenha = findViewById(R.id.campoSenha)
        buttonSalvar = findViewById(R.id.buttonSalvar)
        funcionarioDAO = FuncionarioDAO(this)

        buttonSalvar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val login = campoLogin.text.toString()
                val senha = campoSenha.text.toString()

                // Verifique se o login e a senha não estão em branco
                if (login.isNotEmpty() && senha.isNotEmpty()) {
                    // Verifique se o funcionário já existe no banco de dados
                    funcionarioDAO.open()
                    val funcionarioExistente = funcionarioDAO.obterFuncionarioPorLogin(login)
                    funcionarioDAO.close()

                    if (funcionarioExistente == null) {
                        // Crie um objeto Funcionario com os dados inseridos
                        val novoFuncionario = Funcionario()
                        novoFuncionario.setLogin(login)
                        novoFuncionario.setSenha(senha)

                        // Insira o funcionário no banco de dados
                        funcionarioDAO.open()
                        val idInserido = funcionarioDAO.inserirFuncionario(novoFuncionario)
                        funcionarioDAO.close()

                        if (idInserido != -1L) {
                            // O funcionário foi inserido com sucesso
                            campoLogin.text.clear()
                            campoSenha.text.clear()
                            exibirMensagem("Funcionário cadastrado com sucesso.")
                            val intent = Intent(this@PaginaCadastrarFuncionario, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            exibirMensagem("Erro ao cadastrar funcionário.")
                        }
                    } else {
                        exibirMensagem("Funcionário com esse login já existe.")
                    }
                } else {
                    exibirMensagem("Preencha todos os campos.")
                }
            }
        })
    }

    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}
