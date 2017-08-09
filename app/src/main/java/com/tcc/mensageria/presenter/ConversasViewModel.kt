package com.tcc.mensageria.presenter

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tcc.mensageria.model.Conversa
import com.tcc.mensageria.model.database.ConversaDao
import com.tcc.mensageria.network.RestRepository
import javax.inject.Inject


class ConversasViewModel : ViewModel() {

    @Inject
    lateinit var restRepository: RestRepository

    @Inject
    lateinit var conversaDao: ConversaDao

    var conversas: MutableLiveData<List<Conversa>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData<List<Conversa>>()
                loadConversas()
            }
            return field
        }


    private fun loadConversas() {
        restRepository.getMensagens({
            Log.d(this::class.simpleName, it.toString())
            conversas?.value = conversaDao.listarTodos().value
        }, {
            Log.d(this::class.simpleName, it.toString())
        })
    }
}