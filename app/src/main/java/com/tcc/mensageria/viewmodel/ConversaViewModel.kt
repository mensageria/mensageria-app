package com.tcc.mensageria.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.tcc.mensageria.database.MensagemDao
import com.tcc.mensageria.model.MensagemDTO
import com.tcc.mensageria.network.SocketRepository
import javax.inject.Inject

class ConversaViewModel : ViewModel() {

    @Inject
    lateinit var mensagemDao: MensagemDao

    @Inject
    lateinit var socketRepository: SocketRepository

    fun getMensagens(idConversa: Long): LiveData<List<MensagemDTO>> {
        return mensagemDao.buscarPorIdConversa(idConversa)
    }

    fun loadConversas(idConversa: Long) {
        socketRepository.getMensagens(idConversa)
    }
}