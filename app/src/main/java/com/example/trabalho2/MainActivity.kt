package com.example.trabalho2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho2.DAO.FuncionarioDAO
import com.example.trabalho2.Entity.Funcionario

class MainActivity : AppCompatActivity() {

    private lateinit var campoLogin: EditText
    private lateinit var campoSenhaFuncionario: EditText
    private lateinit var funcionarioDAO: FuncionarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        campoLogin = findViewById(R.id.campoLogin)
        campoSenhaFuncionario = findViewById(R.id.campoSenhaFuncionario)
        funcionarioDAO = FuncionarioDAO(this)

        val botaoCadastrarFuncionario = findViewById<Button>(R.id.buttonCadastroFuncionario)
        val botaoLogin = findViewById<Button>(R.id.botaoLogin)

        botaoCadastrarFuncionario.setOnClickListener {
            val intent = Intent(this, PaginaCadastrarFuncionario::class.java)
            startActivity(intent)
        }

        botaoLogin.setOnClickListener {
            val login = campoLogin.text.toString()
            val senha = campoSenhaFuncionario.text.toString()

            // Verifique se o funcionário existe no banco de dados
            funcionarioDAO.open()
            val funcionario = funcionarioDAO.obterFuncionarioPorLoginESenha(login, senha)
            funcionarioDAO.close()

            if (funcionario != null) {
                // O funcionário foi encontrado, exiba uma mensagem de boas-vindas
                val mensagemBoasVindas = "Bem-vindo, ${funcionario.getLogin()}"
                Toast.makeText(this, mensagemBoasVindas, Toast.LENGTH_SHORT).show()

                // Redirecione para a próxima tela
                val intent = Intent(this, TelaGerenciamento::class.java)
                startActivity(intent)
            } else {
                // Funcionário não encontrado, exiba uma mensagem de erro
                Toast.makeText(this, "Login ou senha incorretos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
