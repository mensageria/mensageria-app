package com.tcc.mensageria.view.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tcc.mensageria.R

/**
 * Adapter do recycler view de mensagens
 */
class MensagensAdapter(cursor: Cursor?, context: Context) : ListaConversaAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaConversaAdapter.ListaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_mensagens, parent, false)
        return super.ListaViewHolder(view);
    }

    override fun onBindViewHolder(holder: ListaConversaAdapter.ListaViewHolder, position: Int) {
//        if (cursor == null) {
//            return
//        }

//        val indexConteudo = cursor!!.getColumnIndex(MensageriaContract.Mensagens.COLUNA_CONTEUDO)
//        val indexData = cursor!!.getColumnIndex(MensageriaContract.Mensagens.COLUNA_DATA_ENVIO)
//        val indexNomeAutor = cursor!!.getColumnIndex(MensageriaContract.Autores.COLUNA_NOME)
//
//        cursor!!.moveToPosition(posicao)
//        holder.conteudo.text = cursor!!.getString(indexConteudo)
//        holder.nomeAutor.text = cursor!!.getString(indexNomeAutor)
//        holder.data.text = Utility.getDataFormatada(cursor!!.getLong(indexData), mContext)
    }

}