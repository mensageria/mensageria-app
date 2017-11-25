package com.tcc.mensageria.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tcc.mensageria.database.ConversaDao
import com.tcc.mensageria.database.DbRepository
import com.tcc.mensageria.model.ConversaDTO
import com.tcc.mensageria.network.RestRepository
import javax.inject.Inject


class ListaConversasViewModel() : ViewModel() {

    @Inject
    lateinit var restRepository: RestRepository

    @Inject
    lateinit var dbRepository: DbRepository

    @Inject
    lateinit var conversaDao: ConversaDao


    val conversas: LiveData<List<ConversaDTO>>
        get() {
            return conversaDao.buscarUltimas()
        }

    public fun loadConversas() {
        restRepository.getMensagens({
            Log.d(this::class.simpleName, "SUCESSO" + it.toString())
            dbRepository.salvarMensagem(it)

        }, {
            Log.d(this::class.simpleName, "ERRO" + it.toString())
        })
    }
}