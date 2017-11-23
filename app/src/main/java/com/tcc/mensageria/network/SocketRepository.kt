package com.tcc.mensageria.network

import android.util.Log
import com.tcc.mensageria.database.AutorDao
import com.tcc.mensageria.database.ConversaDao
import com.tcc.mensageria.database.MensagemDao
import com.tcc.mensageria.model.MensagemPOJO
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import ua.naiksoftware.stomp.client.StompClient
import javax.inject.Inject

class SocketRepository @Inject constructor(private val stompClient: StompClient,
                                           private val mensagemDao: MensagemDao,
                                           private val conversaDao: ConversaDao, private val autorDao: AutorDao) {

    init {
        stompClient.connect()
    }

    fun getMensagens(idConversa: Long) {
        stompClient.topic("/topic/mensagens/conversa/$idConversa").subscribe { topicMessage ->
            Log.d(this::class.simpleName, topicMessage.getPayload())
        }
    }

    //TODO alterar quando estiver pronta a api
    fun salvarMensagem(mensagemPOJO: MensagemPOJO) {
        launch(CommonPool) {
            autorDao.inserir(mensagemPOJO.autor)
            conversaDao.inserir(mensagemPOJO.chat)
            mensagemDao.inserir(mensagemPOJO.getMensagem())
        }
    }
}