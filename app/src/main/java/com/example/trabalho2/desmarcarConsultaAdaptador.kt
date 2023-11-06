package com.example.trabalho2

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho2.DAO.ConsultaDAO
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

    override fun onBindViewHolder(holder: desmarcarConsultaAdaptador.desmarcarConsultaVerHolder, position: Int) {
        val std = stdList[position]
        holder.bindVer(std)
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

        fun bindVer(std: Consulta){
            nomeClienteDesmarcarConsulta.text = std.idCliente.toString()
            nomeVeterinarioDesmarcarConsulta.text = std.idVeterinario.toString()
        }
    }
}