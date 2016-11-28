package com.tcc.mensageria.controller;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.mensageria.R;
import com.tcc.mensageria.model.MensageriaContract;
import com.tcc.mensageria.utils.Utility;

/**
 * Adapter do recycler view de mensagens
 */
public class MensagensAdapter extends ListaConversaAdapter {

    public MensagensAdapter(Cursor cursor, Context context) {
        super(cursor, context);
    }

    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mensagens, parent, false);
        return new ListaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int posicao) {
        if (mCursor == null) {
            return;
        }

        int indexConteudo = mCursor.getColumnIndex(MensageriaContract.Mensagens.COLUNA_CONTEUDO);
        int indexData = mCursor.getColumnIndex(MensageriaContract.Mensagens.COLUNA_DATA_ENVIO);
        int indexNomeAutor = mCursor.getColumnIndex(MensageriaContract.Autores.COLUNA_NOME);

        mCursor.moveToPosition(posicao);
        holder.conteudo.setText(mCursor.getString(indexConteudo));
        holder.nomeAutor.setText(mCursor.getString(indexNomeAutor));
        holder.data.setText(Utility.getDataFormatada(mCursor.getLong(indexData),mContext));
    }

}