package com.tcc.mensageria.view.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcc.mensageria.R;
import com.tcc.mensageria.view.adapter.MensagensAdapter;
import com.tcc.mensageria.model.MensageriaContract;


public class ConversaFragment extends Fragment implements MensagensAdapter.ItemClickCallback, LoaderManager.LoaderCallbacks<Cursor> {

    final String TAG = this.getClass().getSimpleName();
    final int LOADER_ID = 1;
    public static String BUNDLE_ID_CONVERSA = "BUNDLE_ID_CONVERSA";

    int mIdConversa = 1;
    RecyclerView mRecyclerView;
    MensagensAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView mViewVazia;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mIdConversa = getArguments().getInt(BUNDLE_ID_CONVERSA);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversa, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lista_mensagens);
        mViewVazia = (TextView) rootView.findViewById(R.id.view_vazia);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MensagensAdapter(null, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickCallback(this);

        return rootView;
    }


    /**
     * Metodo para verificar se o adapter tem dados para popular a view
     * caso nao haja dados uma mensagem é mostrada
     *
     * @return false se a lista puder ser populada e true se nao puder
     */
    private boolean listaEstaVazia() {
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
    public void onItemClick(int p) {

    }

    @Override
    public void onSecondaryIconClick(int p) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] COLUNAS = {
                MensageriaContract.Conversas.NOME_TABELA + "." + MensageriaContract.Mensagens._ID,
                MensageriaContract.Mensagens.COLUNA_DATA_ENVIO,
                MensageriaContract.Mensagens.COLUNA_CONTEUDO,
                MensageriaContract.Autores.COLUNA_NOME,
                MensageriaContract.Conversas.COLUNA_TITULO
        };

        //conversas._id = mensagens.fk_conversa and conversas._id = x and mensagens.fk_autor = autores._id group by mensagens._id
        String selection = MensageriaContract.Conversas.NOME_TABELA + "." +
                MensageriaContract.Conversas._ID +
                " = " + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA +

                " AND " + MensageriaContract.Conversas.NOME_TABELA + "." +
                MensageriaContract.Conversas._ID + " = " + mIdConversa +

                " AND " + MensageriaContract.Mensagens.COLUNA_FK_AUTOR + " = " +
                MensageriaContract.Autores.NOME_TABELA + "." + MensageriaContract.Autores._ID +
                ") GROUP BY (" + MensageriaContract.Mensagens.NOME_TABELA + "."
                + MensageriaContract.Mensagens._ID;

        String orderBy = MensageriaContract.Mensagens.COLUNA_DATA_ENVIO + " ASC";

        return new CursorLoader(getActivity(),
                MensageriaContract.Conversas.buildConversacomAutorEMensagem(),
                COLUNAS,
                selection,
                null,
                orderBy
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        listaEstaVazia();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}