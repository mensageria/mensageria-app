package com.tcc.mensageria.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tcc.mensageria.R
import com.tcc.mensageria.model.ConversaDTO

/**
 * Adapter do recycler view de conversas
 */
open class ListaConversaAdapter() : RecyclerView.Adapter<ListaConversaAdapter.ListaViewHolder>() {
    internal val TAG = this.javaClass.simpleName

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
//        holder.conteudo.text =
        holder.nomeAutor.text = conversa.autor
        holder.titulo.text = conversa.titulo
        holder.data.text = conversa.dataEnvio.toString()
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
        //final ImageView favorito;
        val data: TextView
        val conteudo: TextView
        val titulo: TextView
        val nomeAutor: TextView
        val container: View

        init {
            foto = itemView.findViewById(R.id.foto) as? ImageView
            //            favorito = (ImageView) itemView.findViewById(R.id.data);
            //            favorito.setOnClickListener(this);
            data = itemView.findViewById(R.id.data) as TextView
            nomeAutor = itemView.findViewById(R.id.nome_autor) as TextView
            conteudo = itemView.findViewById(R.id.conteudo) as TextView
            titulo = itemView.findViewById(R.id.titulo) as TextView
            container = itemView.findViewById(R.id.container_item_lista)
            container.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v.id == R.id.container_item_lista) {
                mItemClickCallback?.onItemClick(adapterPosition)
            } else {
                mItemClickCallback?.onSecondaryIconClick(adapterPosition)
            }
        }
    }

    /**
     * interface que deve ser implementada para lidar com os cliques nos itens da lista
     */
    interface ItemClickCallback {
        fun onItemClick(p: Int)

        fun onSecondaryIconClick(p: Int)
    }
}