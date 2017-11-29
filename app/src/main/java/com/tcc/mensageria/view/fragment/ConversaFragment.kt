package com.tcc.mensageria.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.tcc.mensageria.R
import com.tcc.mensageria.di.ApplicationModule
import com.tcc.mensageria.di.DaggerMensageriaComponent
import com.tcc.mensageria.model.MensagemDTO
import com.tcc.mensageria.view.adapter.ConversaAdapter
import com.tcc.mensageria.viewmodel.ConversaViewModel
import kotlinx.android.synthetic.main.fragment_conversa.view.*


class ConversaFragment : Fragment() {

    lateinit internal var mRecyclerView: RecyclerView
    lateinit internal var mAdapter: ConversaAdapter
    lateinit internal var mLayoutManager: RecyclerView.LayoutManager
    lateinit internal var mViewVazia: TextView
    lateinit internal var mViewModel: ConversaViewModel

    internal var mIdConversa = 0L

    lateinit private var mInputMessageView: EditText

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disconnect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mViewModel = ViewModelProviders.of(this).get(ConversaViewModel::class.java)

        val mensageriaComponent = DaggerMensageriaComponent.builder()
                .applicationModule(ApplicationModule(context))
                .build()
        mensageriaComponent.inject(mViewModel)

        mIdConversa = arguments.getLong(BUNDLE_ID_CONVERSA)

        mViewModel.getMensagens(mIdConversa).observe(this, Observer<List<MensagemDTO>> { mensagens ->
            mAdapter.dados = mensagens ?: mAdapter.dados
            mRecyclerView.smoothScrollToPosition(mAdapter.dados.size - 1)
        })

        mViewModel.loadConversas(mIdConversa)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_conversa, container, false)
        mRecyclerView = rootView.lista_mensagens
        mViewVazia = rootView.view_vazia

        mLayoutManager = LinearLayoutManager(activity)
        (mLayoutManager as LinearLayoutManager).stackFromEnd = true
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = ConversaAdapter(activity)
        mRecyclerView.adapter = mAdapter

        mRecyclerView

        mInputMessageView = rootView.mensagem_input
        mInputMessageView.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, event ->
            var handled = false
            if (id == EditorInfo.IME_ACTION_SEND) {
                enviarMensagem()
                handled = true
            }
            handled
        })

        val sendButton = rootView.botao_enviar
        sendButton.setOnClickListener { enviarMensagem() }

        return rootView
    }

    private fun enviarMensagem() {
        mViewModel.sendMensagem(mIdConversa, mInputMessageView.text.toString())
        mInputMessageView.text.clear()
    }

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

    companion object {
        var BUNDLE_ID_CONVERSA = "BUNDLE_ID_CONVERSA"
    }
}