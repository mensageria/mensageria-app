package com.tcc.mensageria.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.tcc.mensageria.database.DbRepository
import com.tcc.mensageria.database.MensagemDao
import com.tcc.mensageria.model.MensagemDTO
import com.tcc.mensageria.network.SocketRepository
import javax.inject.Inject

class ConversaViewModel : ViewModel() {

    @Inject
    lateinit protected var mensagemDao: MensagemDao

    @Inject
    lateinit protected var socketRepository: SocketRepository

    @Inject
    lateinit protected var dbRepository: DbRepository

    fun getMensagens(idConversa: Long): LiveData<List<MensagemDTO>> {
        return mensagemDao.buscarPorIdConversa(idConversa)
    }

    fun loadConversas(idConversa: Long) {
        socketRepository.getMensagens(idConversa, {
            it?.let { it1 -> dbRepository.salvarMensagem(it1) }
        })
    }

    fun connect() {
        socketRepository.connect()
    }

    fun disconnect() {
        socketRepository.disconnect()
    }
}