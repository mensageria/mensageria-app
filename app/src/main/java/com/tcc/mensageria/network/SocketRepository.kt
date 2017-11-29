package com.tcc.mensageria.network

import com.google.gson.Gson
import com.tcc.mensageria.model.MensagemPOJO
import ua.naiksoftware.stomp.client.StompClient
import javax.inject.Inject

class SocketRepository @Inject constructor(private val stompClient: StompClient) {

    init {
        connect()
    }

    fun connect() {
        stompClient.connect()
    }

    fun disconnect() {
        stompClient.disconnect()
    }

    fun getMensagens(idConversa: Long, callback: (MensagemPOJO?) -> Unit) {
        stompClient.topic("/topic/mensagens/conversa/$idConversa").subscribe {
            try {
                val dados = Gson().fromJson(it.payload, MensagemPOJO::class.java)
                callback(dados)
            } catch (throwable: Throwable) {
            }
        }
    }

    fun sendMensagem(idConversa: Long, mensagemPOJO: MensagemPOJO) {
        val json = Gson().toJson(mensagemPOJO)
        stompClient.send("/app/enviar/mensagens/conversa/$idConversa", json).subscribe()
    }

}