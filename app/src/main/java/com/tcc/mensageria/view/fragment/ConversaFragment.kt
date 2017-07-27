package com.tcc.mensageria.view.fragment


import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.tcc.mensageria.R
import com.tcc.mensageria.view.adapter.ListaConversaAdapter
import com.tcc.mensageria.view.adapter.MensagensAdapter


class ConversaFragment : Fragment(), ListaConversaAdapter.ItemClickCallback, LoaderManager.LoaderCallbacks<Cursor> {

    internal val TAG = this.javaClass.simpleName
    internal val LOADER_ID = 1


    lateinit internal var mRecyclerView: RecyclerView
    lateinit internal var mAdapter: MensagensAdapter
    lateinit internal var mLayoutManager: RecyclerView.LayoutManager
    lateinit internal var mViewVazia: TextView

    internal var mIdConversa: Int = 0

    private var mInputMessageView: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mIdConversa = arguments.getInt(BUNDLE_ID_CONVERSA)

        loaderManager.initLoader(LOADER_ID, null, this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_conversa, container, false)
        mRecyclerView = rootView.findViewById(R.id.lista_mensagens) as RecyclerView
        mViewVazia = rootView.findViewById(R.id.view_vazia) as TextView

        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = MensagensAdapter(null, activity)
        mRecyclerView.adapter = mAdapter
        mAdapter.setItemClickCallback(this)

        mInputMessageView = rootView.findViewById(R.id.mensagem_input) as EditText
        mInputMessageView!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, event ->
            if (id == R.id.send || id == EditorInfo.IME_NULL) {
                enviarMensagem()
                return@OnEditorActionListener true
            }
            false
        })

        val sendButton = rootView.findViewById(R.id.botao_enviar) as ImageButton
        sendButton.setOnClickListener { enviarMensagem() }

        return rootView
    }

    private fun enviarMensagem() {}

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

    override fun onItemClick(p: Int) {

    }

    override fun onSecondaryIconClick(p: Int) {

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
//        val COLUNAS = arrayOf(MensageriaContract.Conversas.NOME_TABELA + "." + MensageriaContract.Mensagens._ID, MensageriaContract.Mensagens.COLUNA_DATA_ENVIO, MensageriaContract.Mensagens.COLUNA_CONTEUDO, MensageriaContract.Autores.COLUNA_NOME, MensageriaContract.Conversas.COLUNA_TITULO)
//
//        //conversas._id = mensagens.fk_conversa and conversas._id = x and mensagens.fk_autor = autores._id group by mensagens._id
//        val selection = MensageriaContract.Conversas.NOME_TABELA + "." +
//                MensageriaContract.Conversas._ID +
//                " = " + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA +
//
//                " AND " + MensageriaContract.Conversas.NOME_TABELA + "." +
//                MensageriaContract.Conversas._ID + " = " + mIdConversa +
//
//                " AND " + MensageriaContract.Mensagens.COLUNA_FK_AUTOR + " = " +
//                MensageriaContract.Autores.NOME_TABELA + "." + MensageriaContract.Autores._ID +
//                ") GROUP BY (" + MensageriaContract.Mensagens.NOME_TABELA + "." + MensageriaContract.Mensagens._ID
//
//        val orderBy = MensageriaContract.Mensagens.COLUNA_DATA_ENVIO + " ASC"
//
//        return CursorLoader(activity,
//                MensageriaContract.Conversas.buildConversacomAutorEMensagem(),
//                COLUNAS,
//                selection, null,
//                orderBy
//        )
        return CursorLoader(null, null, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter.swapCursor(data)
        listaEstaVazia()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }

    companion object {
        var BUNDLE_ID_CONVERSA = "BUNDLE_ID_CONVERSA"
    }
}