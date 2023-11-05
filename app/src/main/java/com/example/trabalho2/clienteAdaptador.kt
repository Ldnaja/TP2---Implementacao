package com.example.trabalho2

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho2.DAO.ClienteDAO
import com.example.trabalho2.Entity.Cliente

class clienteAdaptador : RecyclerView.Adapter<clienteAdaptador.clienteVerHolder>(){

    private var stdList: ArrayList<Cliente> = ArrayList()
    private var onClickDeleteItem: ((Int) -> Unit)? = null

    fun addItems(stdList: ArrayList<Cliente>){
        this.stdList = stdList
        notifyDataSetChanged()
    }

    fun setOnClickDeleteItem(callback: (Int) -> Unit){
        onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = clienteVerHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_clientes, parent, false)
    )

    override fun onBindViewHolder(holder: clienteVerHolder, position: Int) {
        val std = stdList[position]
        holder.bindVer(std)
        holder.botaoDeletar.setOnClickListener {
            onClickDeleteItem?.invoke(std.idCliente)
        }
    }


    override fun getItemCount(): Int {
        return stdList.size
    }


    class clienteVerHolder(var view: View) : RecyclerView.ViewHolder(view){
        private var nomeCliente = view.findViewById<TextView>(R.id.nomeCliente)
        private var cpfCliente = view.findViewById<TextView>(R.id.cpfCliente)
        private var nomeAnimal = view.findViewById<TextView>(R.id.nomeAnimal)
        private var racaAnimal = view.findViewById<TextView>(R.id.racaAnimal)
         var botaoDeletar = view.findViewById<Button>(R.id.botaoRemoverCliente)

        fun bindVer(std: Cliente){
            nomeCliente.text = std.nome
            cpfCliente.text = std.cpf
            nomeAnimal.text = std.animal
            racaAnimal.text = std.racaAnimal
        }
    }
}