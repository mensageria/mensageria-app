package com.tcc.mensageria.view.adapter

import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tcc.mensageria.R

/**
 * Adapter do recycler view de conversas
 */
open class ListaConversaAdapter(cursor: Cursor?, var mContext: Context) : RecyclerView.Adapter<ListaConversaAdapter.ListaViewHolder>() {
    internal val TAG = this.javaClass.simpleName

    /**
     * @return Cursor com os dados que o adapter usará para popular o recycler view
     */
    var cursor: Cursor? = null
        internal set
    internal var mDataValid: Boolean = false
    internal var mRowIdColumn: Int = 0
    internal var mDataSetObserver: DataSetObserver? = null
    internal var mItemClickCallback: ItemClickCallback? = null

    init {
        mDataSetObserver = NotifyingDataSetObserver()
        swapCursor(cursor)
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

    override fun onBindViewHolder(holder: ListaViewHolder, posicao: Int) {
        if (cursor == null) {
            return
        }

//        val indexConteudo = cursor!!.getColumnIndex(MensageriaContract.Mensagens.COLUNA_CONTEUDO)
//        val indexData = cursor!!.getColumnIndex(MensageriaContract.Mensagens.COLUNA_DATA_ENVIO)
//        val indexNomeAutor = cursor!!.getColumnIndex(MensageriaContract.Autores.COLUNA_NOME)
//        val indexTitulo = cursor!!.getColumnIndex(MensageriaContract.Conversas.COLUNA_TITULO)

//        cursor!!.moveToPosition(posicao)
//        holder.conteudo.text = cursor!!.getString(indexConteudo)
//        holder.nomeAutor.text = cursor!!.getString(indexNomeAutor)
//        holder.titulo?.text = cursor!!.getString(indexTitulo)
//        holder.data.text = Utility.getDataFormatada(cursor!!.getLong(indexData), mContext)
    }

    override fun getItemCount(): Int {
        if (mDataValid && cursor != null) {
            return cursor!!.count
        }
        return 0
    }

    override fun getItemId(position: Int): Long {
        if (mDataValid && cursor != null && cursor!!.moveToPosition(position)) {
            return cursor!!.getLong(mRowIdColumn)
        }
        return 0
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    /**
     * Muda o cursor, se se ele ja exister será fechado
     */
    fun changeCursor(cursor: Cursor) {
        val old = swapCursor(cursor)
        old?.close()
    }

    /**
     * Troca o cursor atual

     * @param newCursor cursor com os novos dados
     * *
     * @return Cursor antigo
     */
    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        val oldCursor = cursor
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver)
        }
        cursor = newCursor
        if (cursor != null) {
            if (mDataSetObserver != null) {
                cursor!!.registerDataSetObserver(mDataSetObserver)
            }
            mRowIdColumn = newCursor!!.getColumnIndexOrThrow("_id")
            mDataValid = true
            notifyDataSetChanged()
        } else {
            mRowIdColumn = -1
            mDataValid = false
            notifyDataSetChanged()
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor
    }

    /**
     * Classe com as referencias para as views
     */
    inner class ListaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val foto: ImageView?
        //final ImageView favorito;
        val data: TextView
        val conteudo: TextView
        val titulo: TextView?
        val nomeAutor: TextView
        val container: View

        init {
            foto = itemView.findViewById(R.id.foto) as? ImageView
            //            favorito = (ImageView) itemView.findViewById(R.id.data);
            //            favorito.setOnClickListener(this);
            data = itemView.findViewById(R.id.data) as TextView
            nomeAutor = itemView.findViewById(R.id.nome_autor) as TextView
            conteudo = itemView.findViewById(R.id.conteudo) as TextView
            titulo = itemView.findViewById(R.id.titulo) as? TextView
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
     * classe para gerenciar a mudança dos dados
     */
    private inner class NotifyingDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            mDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            mDataValid = false
            notifyDataSetChanged()
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