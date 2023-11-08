package com.example.trabalho2

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho2.DAO.ClienteDAO
import com.example.trabalho2.DAO.ConsultaDAO
import com.example.trabalho2.DAO.VeterinarioDAO
import com.example.trabalho2.Entity.Consulta

class desmarcarConsultaAdaptador : RecyclerView.Adapter<desmarcarConsultaAdaptador.desmarcarConsultaVerHolder>() {

    private var stdList: ArrayList<Consulta> = ArrayList()
    private var onClickDeleteItem: ((Int) -> Unit)? = null

    fun addItems(stdList: ArrayList<Consulta>){
        this.stdList = stdList
        notifyDataSetChanged()
    }

    fun setOnClickDeleteItem(callback: (Int) -> Unit){
        onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = desmarcarConsultaVerHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_desmarcar_consulta, parent, false)
    )

    override fun onBindViewHolder(holder: desmarcarConsultaVerHolder, position: Int) {
        val std = stdList[position]

        // Buscar o nome do cliente com base no ID
        val clienteDAO = ClienteDAO(holder.itemView.context)
        clienteDAO.open()
        val cliente = clienteDAO.obterCliente(std.idCliente.toLong())
        clienteDAO.close()

        // Buscar o nome do veterinário com base no ID
        val veterinarioDAO = VeterinarioDAO(holder.itemView.context)
        veterinarioDAO.open()
        val veterinario = veterinarioDAO.obterVeterinario(std.idVeterinario.toLong())
        veterinarioDAO.close()

        holder.bindVer(std, cliente?.nome, veterinario?.nome)
        holder.botaoDeletar.setOnClickListener {
            onClickDeleteItem?.invoke(std.idConsulta)
        }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class desmarcarConsultaVerHolder(var view: View) : RecyclerView.ViewHolder(view){
        private var nomeClienteDesmarcarConsulta = view.findViewById<TextView>(R.id.nomeClienteDesmarcarConsulta)
        private var nomeVeterinarioDesmarcarConsulta = view.findViewById<TextView>(R.id.nomeVeterinarioDesmarcarConsulta)
        var botaoDeletar = view.findViewById<Button>(R.id.botaoDesmarcarConsulta)

        fun bindVer(std: Consulta, nomeCliente: String?, nomeVeterinario: String?) {
            nomeClienteDesmarcarConsulta.text = "Nome Cliente: " + (nomeCliente ?: "Cliente não encontrado")
            nomeVeterinarioDesmarcarConsulta.text = "Nome Veterinário: " + (nomeVeterinario ?: "Veterinário não encontrado")
        }
    }
}
