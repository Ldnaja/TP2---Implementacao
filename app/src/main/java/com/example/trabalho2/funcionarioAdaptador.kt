package com.example.trabalho2

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho2.DAO.FuncionarioDAO
import com.example.trabalho2.Entity.Funcionario

class funcionarioAdaptador : RecyclerView.Adapter<funcionarioAdaptador.funcionarioVerHolder>(){

    private var stdList: ArrayList<Funcionario> = ArrayList()
    private var onClickDeleteItem: ((Int) -> Unit)? = null

    fun addItems(stdList: ArrayList<Funcionario>){
        this.stdList = stdList
        notifyDataSetChanged()
    }

    fun setOnClickDeleteItem(callback: (Int) -> Unit){
        onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = funcionarioVerHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_funcionario, parent, false)
    )

    override fun onBindViewHolder(holder: funcionarioVerHolder, position: Int) {
        val std = stdList[position]
        holder.bindVer(std)
        holder.botaoDeletar.setOnClickListener {
            onClickDeleteItem?.invoke(std.idFuncionario)
        }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class funcionarioVerHolder(var view: View) : RecyclerView.ViewHolder(view){
        private var loginFuncionario = view.findViewById<TextView>(R.id.loginFuncionario)
        var botaoDeletar = view.findViewById<Button>(R.id.botaoRemoverFuncionario)

        fun bindVer(std: Funcionario){
            loginFuncionario.text = std.login
        }
    }
}