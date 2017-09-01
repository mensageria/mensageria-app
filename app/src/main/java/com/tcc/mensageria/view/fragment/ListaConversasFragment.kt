package com.tcc.mensageria.view.fragment

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tcc.mensageria.R
import com.tcc.mensageria.model.ConversaDTO
import com.tcc.mensageria.presenter.ListaConversasViewModel
import com.tcc.mensageria.service.DaggerMensageriaComponent
import com.tcc.mensageria.service.DatabaseModule
import com.tcc.mensageria.service.RetrofitModule
import com.tcc.mensageria.sync.MensageriaSyncAdapter
import com.tcc.mensageria.view.activity.ConversaActivity
import com.tcc.mensageria.view.adapter.ListaConversaAdapter

/**
 * Fragmento que contem uma lista de mensagens
 */
class ListaConversasFragment : LifecycleFragment(), ListaConversaAdapter.ItemClickCallback {

    lateinit internal var mRecyclerView: RecyclerView
    lateinit internal var mAdapter: ListaConversaAdapter
    lateinit internal var mLayoutManager: RecyclerView.LayoutManager
    lateinit internal var mViewVazia: TextView
    lateinit internal var mViewModel: ListaConversasViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mViewModel = ViewModelProviders.of(this).get(ListaConversasViewModel::class.java)

        val mensageriaComponent = DaggerMensageriaComponent.builder()
                .retrofitModule(RetrofitModule(activity))
                .databaseModule(DatabaseModule(activity))
                .build()
        mensageriaComponent.inject(mViewModel)
        mViewModel.loadConversas()
        mViewModel.conversas.observe(this, Observer<List<ConversaDTO>> { conversas ->
            if (conversas != null) {
                mAdapter.dados = conversas
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_refresh) {
            mViewModel.loadConversas()
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
        mAdapter = ListaConversaAdapter(activity)
        mRecyclerView.adapter = mAdapter
        mAdapter.setItemClickCallback(this)

        return rootView
    }

    override fun onItemClick(p: Int) {
        val item = mAdapter.dados.get(p)
        val i = Intent(activity, ConversaActivity::class.java)
        i.putExtra(ConversaFragment.BUNDLE_ID_CONVERSA, item.id)
        startActivity(i)
    }

    override fun onSecondaryIconClick(p: Int) {}

    /**
     * Metodo para verificar se o adapter tem dados para popular a view
     * caso nao haja dados uma mensagem é mostrada

     * @return false se a lista puder ser populada e true se nao puder
     */
    private fun listaEstaVazia(): Boolean {
        if (mAdapter.mDataValid) {
            mRecyclerView.visibility = View.VISIBLE
            mViewVazia.visibility = View.GONE
            return false
        } else {
            mRecyclerView.visibility = View.GONE
            mViewVazia.visibility = View.VISIBLE
            return true
        }
    }
}
