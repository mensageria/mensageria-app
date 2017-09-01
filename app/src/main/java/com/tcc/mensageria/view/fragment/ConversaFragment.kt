package com.tcc.mensageria.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
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


class ConversaFragment : Fragment(), ListaConversaAdapter.ItemClickCallback {

    internal val TAG = this.javaClass.simpleName

    lateinit internal var mRecyclerView: RecyclerView
    lateinit internal var mAdapter: MensagensAdapter
    lateinit internal var mLayoutManager: RecyclerView.LayoutManager
    lateinit internal var mViewVazia: TextView

    internal var mIdConversa = 0L

    private var mInputMessageView: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mIdConversa = arguments.getLong(BUNDLE_ID_CONVERSA)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_conversa, container, false)
        mRecyclerView = rootView.findViewById(R.id.lista_mensagens) as RecyclerView
        mViewVazia = rootView.findViewById(R.id.view_vazia) as TextView

        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = MensagensAdapter(activity)
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
    //    private fun listaEstaVazia(): Boolean {
//        if (mAdapter.cursor!!.moveToFirst()) {
//            mRecyclerView.visibility = View.VISIBLE
//            mViewVazia.visibility = View.GONE
//            return false
//        } else {
//            mRecyclerView.visibility = View.GONE
//            mViewVazia.visibility = View.VISIBLE
//            return true
//        }
//    }

    override fun onItemClick(p: Int) {

    }

    override fun onSecondaryIconClick(p: Int) {

    }

    companion object {
        var BUNDLE_ID_CONVERSA = "BUNDLE_ID_CONVERSA"
    }
}