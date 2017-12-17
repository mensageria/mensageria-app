package com.tcc.mensageria.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import com.tcc.mensageria.BuildConfig
import com.tcc.mensageria.database.DbRepository
import com.tcc.mensageria.model.Autor
import com.tcc.mensageria.model.Conversa
import com.tcc.mensageria.model.MensagemDTO
import com.tcc.mensageria.model.MensagemPOJO
import com.tcc.mensageria.network.SocketRepository
import javax.inject.Inject

class ConversaViewModel : ViewModel() {

    @Inject
    lateinit protected var socketRepository: SocketRepository

    @Inject
    lateinit protected var dbRepository: DbRepository

    @Inject
    lateinit protected var sharedPreferences: SharedPreferences

    fun getMensagens(idConversa: Long): LiveData<List<MensagemDTO>> {
        return dbRepository.buscarPorIdConversa(idConversa)
    }

    fun loadConversas(idConversa: Long) {
        socketRepository.getMensagens(idConversa, {
            it?.let { it1 -> dbRepository.salvarMensagem(it1) }
        })
    }

    fun sendMensagem(idConversa: Long, conteudo: String) {
        val autorId = sharedPreferences.getLong(com.tcc.mensageria.BuildConfig.autor_key, 0)
        val mensagem = MensagemPOJO(conteudo = conteudo, autor = Autor(autorId), chat = Conversa(idConversa))
        socketRepository.sendMensagem(idConversa, mensagem, { dbRepository.salvarMensagem(mensagem) })
    }

    fun connect() {
        socketRepository.connect()
    }

    fun disconnect() {
        socketRepository.disconnect()
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(BuildConfig.autor_key, 0)
    }
}