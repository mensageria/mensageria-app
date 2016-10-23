package com.tcc.mensageria.view;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcc.mensageria.R;
import com.tcc.mensageria.controller.ListaAdapter;
import com.tcc.mensageria.controller.sync.MensageriaSyncAdapter;
import com.tcc.mensageria.model.MensageriaContract;

public class MensagensFragment extends Fragment
        implements ListaAdapter.ItemClickCallback, LoaderManager.LoaderCallbacks<Cursor> {
    final String TAG = this.getClass().getSimpleName();
    final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    final String EXTRA_MENSAGEM = "EXTRA_MENSAGEM";
    final String EXTRA_REMETENTE = "EXTRA_REMETENTE";
    final int LOADER_ID = 0;

    final String[] COLUNAS_MENSAGEM = {
            MensageriaContract.Mensagens.NOME_TABELA + "." + MensageriaContract.Mensagens._ID,
            MensageriaContract.Mensagens.COLUNA_TITULO,
            MensageriaContract.Mensagens.COLUNA_CONTEUDO,
            MensageriaContract.Mensagens.COLUNA_FAVORITO,
            MensageriaContract.Mensagens.COLUNA_FK_REMETENTE,
            MensageriaContract.Remetentes.COLUNA_NOME,
            MensageriaContract.Remetentes.COLUNA_EMAIL
    };
    RecyclerView mRecyclerView;
    ListaAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView mViewVazia;
    Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            MensageriaSyncAdapter.syncImmediately(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mensagens, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lista_mensagens);
        mViewVazia = (TextView) rootView.findViewById(R.id.view_vazia);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListaAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickCallback(this);

        return rootView;
    }

    @Override
    public void onItemClick(int p) {
        Cursor item = mAdapter.getCursor();
        item.moveToPosition(p);
        Intent i = new Intent(mContext, com.tcc.mensageria.view.DetalhesMensagemActivity.class);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_MENSAGEM,
                item.getString(item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_CONTEUDO)));
        extras.putString(EXTRA_REMETENTE, item.getString(item.getColumnIndex(MensageriaContract.Remetentes.COLUNA_EMAIL)));

        i.putExtra(BUNDLE_EXTRAS, extras);

        startActivity(i);
    }

    @Override
    public void onSecondaryIconClick(int p) {

        Cursor item = mAdapter.getCursor();
        int indexId = item.getColumnIndex(MensageriaContract.Mensagens._ID);
        int indexConteudo = item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_CONTEUDO);
        int indexTitulo = item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_TITULO);
        int indexFavorito = item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_FAVORITO);
        int indexRemetente = item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_FK_REMETENTE);

        item.moveToPosition(p);

        ContentValues contentValues = new ContentValues();
        int id = item.getInt(indexId);
        contentValues.put(MensageriaContract.Mensagens.COLUNA_CONTEUDO, item.getString(indexConteudo));
        contentValues.put(MensageriaContract.Mensagens.COLUNA_TITULO, item.getString(indexTitulo));
        int favorito = item.getInt(indexFavorito) == 0 ? 1 : 0;
        contentValues.put(MensageriaContract.Mensagens.COLUNA_FAVORITO, favorito);
        contentValues.put(MensageriaContract.Mensagens.COLUNA_FK_REMETENTE, item.getInt(indexRemetente));

        Uri uri = MensageriaContract.Mensagens.CONTENT_URI;
        getActivity().getContentResolver().update(uri, contentValues, "_id = ?",
                new String[]{Integer.toString(id)});
    }

    private boolean listaVazia() {
        if (mAdapter.getCursor().moveToFirst()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mViewVazia.setVisibility(View.GONE);
            return false;
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mViewVazia.setVisibility(View.VISIBLE);
            return true;
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MensageriaContract.Mensagens.buildMensagemComRemetente(),
                COLUNAS_MENSAGEM,
                null,
                null,
                COLUNAS_MENSAGEM[0] + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        listaVazia();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
