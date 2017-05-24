package com.tcc.mensageria.view.fragment

import android.app.Fragment
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tcc.mensageria.R
import com.tcc.mensageria.model.MensageriaContract
import com.tcc.mensageria.sync.MensageriaSyncAdapter
import com.tcc.mensageria.view.activity.ConversaActivity
import com.tcc.mensageria.view.adapter.ListaConversaAdapter

/**
 * Fragmento que contem uma lista de mensagens
 */
class ListaConversasFragment : Fragment(), ListaConversaAdapter.ItemClickCallback, LoaderManager.LoaderCallbacks<Cursor> {

    internal val TAG = this.javaClass.simpleName
    internal val LOADER_ID = 0

    lateinit internal var mRecyclerView: RecyclerView
    lateinit internal var mAdapter: ListaConversaAdapter
    lateinit internal var mLayoutManager: RecyclerView.LayoutManager
    lateinit internal var mViewVazia: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        loaderManager.initLoader(LOADER_ID, null, this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_refresh) {
            MensageriaSyncAdapter.syncImmediately(activity)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_lista_conversas, container, false)
        mRecyclerView = rootView.findViewById(R.id.lista_conversas) as RecyclerView
        mViewVazia = rootView.findViewById(R.id.view_vazia) as TextView

        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = ListaConversaAdapter(null, activity)
        mRecyclerView.adapter = mAdapter
        mAdapter.setItemClickCallback(this)

        return rootView
    }

    override fun onItemClick(p: Int) {
        val item = mAdapter.cursor
        item!!.moveToPosition(p)
        val i = Intent(activity, ConversaActivity::class.java)
        i.putExtra(ConversaFragment.BUNDLE_ID_CONVERSA,
                item.getInt(item.getColumnIndex(MensageriaContract.Conversas._ID)))
        startActivity(i)
    }

    override fun onSecondaryIconClick(p: Int) {}

    /**
     * Metodo para verificar se o adapter tem dados para popular a view
     * caso nao haja dados uma mensagem é mostrada

     * @return false se a lista puder ser populada e true se nao puder
     */
    private fun listaEstaVazia(): Boolean {
        if (mAdapter.cursor!!.moveToFirst()) {
            mRecyclerView.visibility = View.VISIBLE
            mViewVazia.visibility = View.GONE
            return false
        } else {
            mRecyclerView.visibility = View.GONE
            mViewVazia.visibility = View.VISIBLE
            return true
        }
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val COLUNAS = arrayOf(MensageriaContract.Conversas.NOME_TABELA + "." + MensageriaContract.Mensagens._ID, MensageriaContract.Mensagens.COLUNA_DATA_ENVIO, MensageriaContract.Mensagens.COLUNA_CONTEUDO, MensageriaContract.Autores.COLUNA_NOME, MensageriaContract.Conversas.COLUNA_TITULO)

        // conversas._id = mensagens.fk_conversa group by fk_conversa
        val selection = MensageriaContract.Conversas.NOME_TABELA + "." +
                MensageriaContract.Conversas._ID +
                " = " + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA +
                ") GROUP BY (" + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA

        val orderBy = MensageriaContract.Mensagens.COLUNA_DATA_ENVIO + " DESC"

        return CursorLoader(activity,
                MensageriaContract.Conversas.buildConversacomAutorEMensagem(),
                COLUNAS,
                selection, null,
                orderBy
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter.swapCursor(data)
        listaEstaVazia()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }
}
