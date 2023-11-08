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

class veterinarioAdaptador : RecyclerView.Adapter<veterinarioAdaptador.veterinarioVerHolder>(){

    private var stdList: ArrayList<Veterinario> = ArrayList()
    private var onClickDeleteItem: ((Int) -> Unit)? = null

    fun addItems(stdList: ArrayList<Veterinario>){
        this.stdList = stdList
        notifyDataSetChanged()
    }

    fun setOnClickDeleteItem(callback: (Int) -> Unit){
        onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = veterinarioVerHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_veterinarios, parent, false)
    )

    override fun onBindViewHolder(holder: veterinarioVerHolder, position: Int) {
        val std = stdList[position]
        holder.bindVer(std)
        holder.botaoDeletar.setOnClickListener {
            onClickDeleteItem?.invoke(std.idVeterinario)
        }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class veterinarioVerHolder(var view: View) : RecyclerView.ViewHolder(view){
        private var nomeVeterinario = view.findViewById<TextView>(R.id.nomeVeterinario)
        private var especializacaoVeterinario = view.findViewById<TextView>(R.id.especializacaoVeterinario)
        private var crmvVeterinario = view.findViewById<TextView>(R.id.crmvVeterinario)
        var botaoDeletar = view.findViewById<Button>(R.id.botaoRemoverVeterinario)

        fun bindVer(std: Veterinario){
            nomeVeterinario.text = "Nome Veterinario: " + std.nome
            especializacaoVeterinario.text = "Especialização: " + std.especializacao
            crmvVeterinario.text = "CRMV do Veterionario: " + std.crmv
        }
    }
}