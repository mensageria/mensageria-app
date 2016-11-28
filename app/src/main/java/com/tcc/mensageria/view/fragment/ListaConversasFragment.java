package com.tcc.mensageria.view.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcc.mensageria.R;
import com.tcc.mensageria.view.adapter.ListaConversaAdapter;
import com.tcc.mensageria.sync.MensageriaSyncAdapter;
import com.tcc.mensageria.model.MensageriaContract;
import com.tcc.mensageria.view.activity.ConversaActivity;

/**
 * Fragmento que contem uma lista de mensagens
 */
public class ListaConversasFragment extends Fragment
        implements ListaConversaAdapter.ItemClickCallback, LoaderManager.LoaderCallbacks<Cursor> {

    final String TAG = this.getClass().getSimpleName();
    final int LOADER_ID = 0;

    RecyclerView mRecyclerView;
    ListaConversaAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView mViewVazia;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View rootView = inflater.inflate(R.layout.fragment_lista_conversas, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lista_conversas);
        mViewVazia = (TextView) rootView.findViewById(R.id.view_vazia);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListaConversaAdapter(null, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickCallback(this);

        return rootView;
    }

    @Override
    public void onItemClick(int p) {
        Cursor item = mAdapter.getCursor();
        item.moveToPosition(p);
        Intent i = new Intent(getActivity(), ConversaActivity.class);
        i.putExtra(ConversaFragment.BUNDLE_ID_CONVERSA,
                item.getInt(item.getColumnIndex(MensageriaContract.Conversas._ID)));
        startActivity(i);
    }

    @Override
    public void onSecondaryIconClick(int p) {
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] COLUNAS = {
                MensageriaContract.Conversas.NOME_TABELA + "." + MensageriaContract.Mensagens._ID,
                MensageriaContract.Mensagens.COLUNA_DATA_ENVIO,
                MensageriaContract.Mensagens.COLUNA_CONTEUDO,
                MensageriaContract.Autores.COLUNA_NOME,
                MensageriaContract.Conversas.COLUNA_TITULO
        };

        // conversas._id = mensagens.fk_conversa group by fk_conversa
        String selection = MensageriaContract.Conversas.NOME_TABELA + "." +
                MensageriaContract.Conversas._ID +
                " = " + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA +
                ") GROUP BY (" + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA;

        String orderBy = MensageriaContract.Mensagens.COLUNA_DATA_ENVIO + " DESC";

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
