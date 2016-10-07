package com.tcc.mensageria.controller;

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


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder> {
    final String TAG = this.getClass().getSimpleName();

    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIdColumn;
    private DataSetObserver mDataSetObserver;
    private ItemClickCallback mItemClickCallback;

    public ListaAdapter(Cursor cursor) {
        mDataSetObserver = new NotifyingDataSetObserver();
        swapCursor(cursor);
    }

    public Cursor getCursor() {
        return mCursor;
    }


    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.mItemClickCallback = itemClickCallback;
    }


    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mensagens, parent, false);
        return new ListaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int posicao) {
        if(mCursor == null){
            return;
        }

        int indexConteudo = mCursor.getColumnIndex(MensageriaContract.Mensagens.COLUNA_CONTEUDO);
        int indextitulo = mCursor.getColumnIndex(MensageriaContract.Mensagens.COLUNA_TITULO);
        int indexFavorito = mCursor.getColumnIndex(MensageriaContract.Mensagens.COLUNA_FAVORITO);
        int indexEmailRemetente = mCursor.getColumnIndex(MensageriaContract.Remetentes.COLUNA_EMAIL);

        mCursor.moveToPosition(posicao);
        holder.conteudo.setText(mCursor.getString(indexConteudo));
        holder.titulo.setText(mCursor.getString(indextitulo));
        holder.emailRemetente.setText(mCursor.getString(indexEmailRemetente));
        if (mCursor.getInt(indexFavorito) == 1) {
            holder.favorito.setImageResource(R.drawable.ic_star_black_36dp);
        } else {
            holder.favorito.setImageResource(R.drawable.ic_star_border_black_36dp);
        }
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
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
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


    class ListaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView foto;
        final ImageView favorito;
        final TextView conteudo;
        final TextView titulo;
        final TextView emailRemetente;
        final View container;

        public ListaViewHolder(View itemView) {
            super(itemView);
            foto = (ImageView) itemView.findViewById(R.id.foto);
            favorito = (ImageView) itemView.findViewById(R.id.favorito);
            favorito.setOnClickListener(this);
            emailRemetente = (TextView) itemView.findViewById(R.id.emailRemetente);
            conteudo = (TextView) itemView.findViewById(R.id.conteudo);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            container = itemView.findViewById(R.id.container_mensagem);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.container_mensagem) {
                mItemClickCallback.onItemClick(getAdapterPosition());
            } else {
                mItemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }
    }

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

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);
    }
}