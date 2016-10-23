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
            //MensageriaSyncAdapter.syncImmediately(getActivity());
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

        //listaVazia();

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

//        Cursor item = mAdapter.getCursor();
//        int indexConteudo = item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_CONTEUDO);
//        int indexTitulo = item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_TITULO);
//        int indexFavorito = item.getColumnIndex(MensageriaContract.Mensagens.COLUNA_FAVORITO);
//        int indexEmailRemetente = item.getColumnIndex(MensageriaContract.Remetentes.COLUNA_EMAIL);
//
//        item.moveToPosition(p);
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MensageriaContract.Mensagens._ID, item.getInt(indexConteudo));
//        contentValues.put(MensageriaContract.Mensagens.COLUNA_CONTEUDO, item.getString(indexConteudo));
//        contentValues.put(MensageriaContract.Mensagens.COLUNA_TITULO, item.getString(indexTitulo));
//        contentValues.put(MensageriaContract.Mensagens.COLUNA_FAVORITO,
//                item.getInt(indexFavorito) == 0 ? 1 : 0);
//        contentValues.put(MensageriaContract.Mensagens.COLUNA_FK_REMETENTE, item.getString(indexEmailRemetente));
//
//        Uri uri = MensageriaContract.Mensagens.CONTENT_URI;
//        getActivity().getContentResolver().update(uri,contentValues,null,null);
    }

    private boolean listaVazia() {
        if (mAdapter.getCursor() == null) {
            mRecyclerView.setVisibility(View.GONE);
            mViewVazia.setVisibility(View.VISIBLE);
            return true;
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mViewVazia.setVisibility(View.GONE);
            return false;
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MensageriaContract.Mensagens.buildMensagemComRemetente();

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                uri,
                COLUNAS_MENSAGEM,
                null,
                null,
                COLUNAS_MENSAGEM[0] + " DESC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
