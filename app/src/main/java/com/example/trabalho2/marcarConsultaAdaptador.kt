package com.example.trabalho2

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho2.DAO.VeterinarioDAO
import com.example.trabalho2.Entity.Veterinario

class marcarConsultaAdaptador : RecyclerView.Adapter<marcarConsultaAdaptador.marcarConsultaVerHolder>(){

    private var stdList: ArrayList<Veterinario> = ArrayList()
    private var onClickItem: ((Int) -> Unit)? = null

    fun addItems(stdList: ArrayList<Veterinario>){
        this.stdList = stdList
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (Int) -> Unit){
        onClickItem = callback
    }

    fun getVeterinarioById(veterinarioId: Int): Veterinario? {
        return stdList.find { it.idVeterinario == veterinarioId }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = marcarConsultaVerHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_consulta_vet, parent, false)
    )

    override fun onBindViewHolder(holder: marcarConsultaVerHolder, position: Int) {
        val std = stdList[position]
        holder.bindVer(std)
        holder.itemView.setOnClickListener {
            onClickItem?.invoke(std.idVeterinario)
        }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class marcarConsultaVerHolder(var view: View) : RecyclerView.ViewHolder(view){
        private var nomeVeterinarioConsulta = view.findViewById<TextView>(R.id.nomeVeterinarioConsulta)
        private var especializacaoVeterinarioConsulta = view.findViewById<TextView>(R.id.especializacaoVeterinarioConsulta)
        private var crmvVeterinarioConsulta = view.findViewById<TextView>(R.id.crmvVeterinarioConsulta)

        fun bindVer(std: Veterinario){
            nomeVeterinarioConsulta.text = "Nome Veterinario: " + std.nome
            especializacaoVeterinarioConsulta.text = "Especialização: " + std.especializacao
            crmvVeterinarioConsulta.text = "CRMV do Veterionario: " + std.crmv
        }
    }
}