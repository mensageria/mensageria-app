package com.tcc.mensageria.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tcc.mensageria.R
import com.tcc.mensageria.model.ConversaDTO
import com.tcc.mensageria.utils.Utility
import kotlinx.android.synthetic.main.item_lista_conversas.view.*

/**
 * Adapter do recycler view de conversas
 */
open class ListaConversaAdapter(val mContext: Context) : RecyclerView.Adapter<ListaConversaAdapter.ListaViewHolder>() {

    internal var mDataValid: Boolean = false
    internal var mItemClickCallback: ItemClickCallback? = null

    var dados: List<ConversaDTO> = ArrayList<ConversaDTO>()
        set (novosDados) {
            if (novosDados != field) {
                field = novosDados
                notifyDataSetChanged()
            }
        }

    /**
     * @param itemClickCallback Callback para lidar com cliques em um item da lista
     */
    fun setItemClickCallback(itemClickCallback: ItemClickCallback) {
        this.mItemClickCallback = itemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_conversas, parent, false)
        return ListaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaViewHolder, position: Int) {
        val conversa = dados.get(position)
        holder.conteudo.text = conversa.conteudo
        holder.nomeAutor.text = conversa.autor
        holder.titulo.text = conversa.titulo
        holder.data.text = Utility.getDataFormatada(conversa.dataEnvio!!.time, mContext)
    }

    override fun getItemCount(): Int {
        return dados.size
    }

    override fun getItemId(position: Int): Long {
        return dados.get(position).id!!
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    /**
     * Classe com as referencias para as views
     */
    inner class ListaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val foto: ImageView?
        val data: TextView
        val conteudo: TextView
        val titulo: TextView
        val nomeAutor: TextView
        val container: View

        init {
            foto = itemView.foto
            data = itemView.data
            nomeAutor = itemView.nome_autor
            conteudo = itemView.conteudo
            titulo = itemView.titulo
            container = itemView.container_item_lista
            container.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v.id == R.id.container_item_lista) {
                mItemClickCallback?.onItemClick(adapterPosition)
            }
        }
    }

    /**
     * interface que deve ser implementada para lidar com os cliques nos itens da lista
     */
    interface ItemClickCallback {
        fun onItemClick(p: Int)
    }
}