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

class MarcarConsulta : AppCompatActivity() {

    private lateinit var campoCPFClienteConsulta: EditText
    private lateinit var campoCRMVVeterinarioConsulta: EditText

    private lateinit var botaoMostrarVeterinariosConsulta: Button
    private lateinit var botaoMarcarConsulta: Button

    private lateinit var consultaDAO: ConsultaDAO
    private lateinit var clienteDAO: ClienteDAO
    private lateinit var veterinarioDAO: VeterinarioDAO

    private lateinit var recyclerView: RecyclerView
    private var adapter: marcarConsultaAdaptador? = null
    private var std: Veterinario? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcar_consulta)

        campoCPFClienteConsulta = findViewById(R.id.campoCPFClienteConsulta)
        campoCRMVVeterinarioConsulta = findViewById(R.id.campoCRMVVeterinarioConsulta)

        botaoMostrarVeterinariosConsulta = findViewById(R.id.botaoMostrarVeterinariosConsulta)
        botaoMarcarConsulta = findViewById(R.id.botaoMarcarConsulta)

        consultaDAO = ConsultaDAO(this)
        clienteDAO = ClienteDAO(this)
        veterinarioDAO = VeterinarioDAO(this)

        initView()
        initRecycler()

        botaoMarcarConsulta.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                val cpf = campoCPFClienteConsulta.text.toString()
                val crmv = campoCRMVVeterinarioConsulta.text.toString()

                if (cpf.isEmpty() || crmv.isEmpty()) {
                    exibirMensagem("Preencha todos os campos!")
                } else {
                    clienteDAO.open()
                    val cliente = clienteDAO.obterClientePorCpf(cpf)
                    clienteDAO.close()

                    veterinarioDAO.open()
                    val veterinario = veterinarioDAO.obterVeterinarioPorCRMV(crmv)
                    veterinarioDAO.close()

                    if (cliente == null) {
                        exibirMensagem("Cliente não encontrado!")
                    } else if (veterinario == null) {
                        exibirMensagem("Veterinário não encontrado!")
                    } else {
                        val idCliente = cliente.getIdCliente()
                        val idVeterinario = veterinario.getIdVeterinario()

                        consultaDAO.open()

                        // Verificar se a consulta já existe
                        if (consultaDAO.consultaExiste(idCliente, idVeterinario)) {
                            exibirMensagem("Essa consulta já foi marcada!")
                        } else {
                            val idConsulta = consultaDAO.inserirConsulta(idCliente, idVeterinario)
                            exibirMensagem("Consulta marcada com sucesso!")
                        }

                        consultaDAO.close()
                    }
                }
            }
        })



        botaoMostrarVeterinariosConsulta.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                getVeterinarios()
            }
        })

        adapter?.setOnClickItem { veterinarioId ->
            val veterinario = adapter?.getVeterinarioById(veterinarioId)
            veterinario?.let{
                Toast.makeText(this, "Veterinario selecionado: ${it.nome}", Toast.LENGTH_SHORT).show()
                campoCRMVVeterinarioConsulta.text = Editable.Factory.getInstance().newEditable(it.crmv)
                std = it
            }
        }
    }

    private fun getVeterinarios(){
        veterinarioDAO.open()
        val veterinarios = veterinarioDAO.obterTodosVeterinarios()
        veterinarioDAO.close()
        adapter?.addItems(veterinarios)
    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = marcarConsultaAdaptador()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        recyclerView = findViewById(R.id.recyclerViewConsulta)
    }

    private fun exibirMensagem(mensagem: String){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

}