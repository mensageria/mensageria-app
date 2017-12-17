package com.tcc.mensageria.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.tcc.mensageria.R
import com.tcc.mensageria.di.ApplicationModule
import com.tcc.mensageria.di.DaggerMensageriaComponent
import com.tcc.mensageria.model.ConversaDTO
import com.tcc.mensageria.view.activity.ConversaActivity
import com.tcc.mensageria.view.adapter.ListaConversaAdapter
import com.tcc.mensageria.viewmodel.ListaConversasViewModel
import kotlinx.android.synthetic.main.fragment_lista_conversas.view.*

/**
 * Fragmento que contem uma lista de mensagens
 */
class ListaConversasFragment : Fragment(), ListaConversaAdapter.ItemClickCallback {

    lateinit internal var mRecyclerView: RecyclerView
    lateinit internal var mAdapter: ListaConversaAdapter
    lateinit internal var mLayoutManager: RecyclerView.LayoutManager
    lateinit internal var mViewVazia: TextView
    lateinit internal var mLoading: ProgressBar
    lateinit internal var mViewModel: ListaConversasViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mViewModel = ViewModelProviders.of(this).get(ListaConversasViewModel::class.java)

        val mensageriaComponent = DaggerMensageriaComponent.builder()
                .applicationModule(ApplicationModule(activity))
                .build()
        mensageriaComponent.inject(mViewModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_refresh) {
            atualizar()
        }
        return super.onOptionsItemSelected(item)
    }

    fun atualizar() {
        mudarLoading(View.VISIBLE)
        mViewModel.loadConversas({ mudarLoading(View.GONE) },
                {
                    Toast.makeText(activity, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show()
                    mudarLoading(View.GONE)
                })
    }

    fun mudarLoading(visibilidade: Int) {
        mLoading.visibility = visibilidade
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_lista_conversas, container, false)
        mRecyclerView = rootView.lista_conversas
        mViewVazia = rootView.view_vazia
        mLoading = rootView.progressBar

        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = ListaConversaAdapter(activity)
        mRecyclerView.adapter = mAdapter
        mAdapter.setItemClickCallback(this)

        atualizar()
        return rootView
    }

    override fun onItemClick(p: Int) {
        val item = mAdapter.dados.get(p)
        val i = Intent(activity, ConversaActivity::class.java)
        i.putExtra(ConversaFragment.BUNDLE_ID_CONVERSA, item.id)
        startActivity(i)
    }

    /**
     * Metodo para verificar se o adapter tem dados para popular a view
     * caso nao haja dados uma mensagem é mostrada
     */
    private fun verificarLista() {
        if (mAdapter.dados.isEmpty()) {
            mRecyclerView.visibility = View.GONE
            mViewVazia.visibility = View.VISIBLE
        } else {
            mRecyclerView.visibility = View.VISIBLE
            mViewVazia.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.conversas.observe(this, Observer<List<ConversaDTO>> { conversas ->
            if (conversas != null) {
                mAdapter.dados = conversas
            }
            verificarLista()
        })
    }

    override fun onPause() {
        super.onPause()
        mViewModel.conversas.removeObservers(this)
    }
}
