package com.tcc.mensageria.view.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tcc.mensageria.R
import com.tcc.mensageria.model.MensagemDTO
import com.tcc.mensageria.utils.formatHour
import kotlinx.android.synthetic.main.item_lista_conversas.view.*

/**
 * Adapter do recycler view de mensagens
 */
class ConversaAdapter(val context: Context) : RecyclerView.Adapter<ConversaAdapter.ViewHolder>() {

    // itens para popular a lista
    var dados: List<MensagemDTO> = ArrayList()
        set (new) {
            if (new != field) {
                field = new
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int = dados.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dados[position]
        holder.data.text = item.dataEnvio?.formatHour()
        holder.conteudo.text = item.conteudo
        holder.nomeAutor.text = item.nomeAutor
        if (item.prioridade > 0) {
            holder.nomeAutor.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_mensagens, parent, false)
        return ViewHolder(view)
    }

    /**
     * Classe com as referencias para as views
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val data: TextView = itemView.data
        val conteudo: TextView = itemView.conteudo
        val nomeAutor: TextView = itemView.nome_autor

    }

}