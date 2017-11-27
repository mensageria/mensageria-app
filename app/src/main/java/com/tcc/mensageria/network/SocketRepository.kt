package com.tcc.mensageria.network

import android.util.Log
import com.google.gson.Gson
import com.tcc.mensageria.model.MensagemPOJO
import ua.naiksoftware.stomp.client.StompClient
import javax.inject.Inject

class SocketRepository @Inject constructor(private val stompClient: StompClient) {

    fun connect() {
        stompClient.connect()
    }

    fun disconnect() {
        stompClient.disconnect()
    }

    fun getMensagens(idConversa: Long, callback: (MensagemPOJO?) -> Unit) {
        stompClient.topic("/topic/mensagens/conversa/$idConversa").subscribe { res ->
            Log.d(this::class.simpleName, res.getPayload())
            try {
                val dados = Gson().fromJson(res.payload, MensagemPOJO::class.java)
                callback(dados)
            } catch (throwable: Throwable) {
            }
        }
    }
}