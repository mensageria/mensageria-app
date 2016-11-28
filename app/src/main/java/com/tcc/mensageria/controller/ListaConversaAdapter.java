package com.tcc.mensageria.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.mensageria.R;
import com.tcc.mensageria.model.MensageriaContract;
import com.tcc.mensageria.utils.Utility;

/**
 * Adapter do recycler view de conversas
 */
public class ListaConversaAdapter extends RecyclerView.Adapter<ListaConversaAdapter.ListaViewHolder> {
    final String TAG = this.getClass().getSimpleName();

    Cursor mCursor;
    boolean mDataValid;
    int mRowIdColumn;
    DataSetObserver mDataSetObserver;
    ItemClickCallback mItemClickCallback;
    Context mContext;

    public ListaConversaAdapter(Cursor cursor, Context context) {
        mContext = context;
        mDataSetObserver = new NotifyingDataSetObserver();
        swapCursor(cursor);
    }

    /**
     * @return Cursor com os dados que o adapter usará para popular o recycler view
     */
    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * @param itemClickCallback Callback para lidar com cliques em um item da lista
     */
    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.mItemClickCallback = itemClickCallback;
    }

    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_conversas, parent, false);
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
        int indexTitulo = mCursor.getColumnIndex(MensageriaContract.Conversas.COLUNA_TITULO);

        mCursor.moveToPosition(posicao);
        holder.conteudo.setText(mCursor.getString(indexConteudo));
        holder.nomeAutor.setText(mCursor.getString(indexNomeAutor));
        holder.titulo.setText(mCursor.getString(indexTitulo));
        holder.data.setText(Utility.getDataFormatada(mCursor.getLong(indexData), mContext));
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    /**
     * Muda o cursor, se se ele ja exister será fechado
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Troca o cursor atual
     *
     * @param newCursor cursor com os novos dados
     * @return Cursor antigo
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    /**
     * Classe com as referencias para as views
     */
    class ListaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView foto;
        //final ImageView favorito;
        final TextView data;
        final TextView conteudo;
        final TextView titulo;
        final TextView nomeAutor;
        final View container;

        public ListaViewHolder(View itemView) {
            super(itemView);
            foto = (ImageView) itemView.findViewById(R.id.foto);
//            favorito = (ImageView) itemView.findViewById(R.id.data);
//            favorito.setOnClickListener(this);
            data = (TextView) itemView.findViewById(R.id.data);
            nomeAutor = (TextView) itemView.findViewById(R.id.nome_autor);
            conteudo = (TextView) itemView.findViewById(R.id.conteudo);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            container = itemView.findViewById(R.id.container_item_lista);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.container_item_lista) {
                mItemClickCallback.onItemClick(getAdapterPosition());
            } else {
                mItemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }
    }

    /**
     * classe para gerenciar a mudança dos dados
     */
    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
        }
    }

    /**
     * interface que deve ser implementada para lidar com os cliques nos itens da lista
     */
    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);
    }
}