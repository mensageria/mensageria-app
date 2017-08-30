package com.tcc.mensageria.model

import java.util.*


data class MensagemPOJO(
        var id: Long,
        var conteudo: String,
        var dataEnvio: Date,
        var autor: Autor,
        var chat: Conversa
) {

    fun getMensagem(): Mensagem {
        return Mensagem(id, conteudo, dataEnvio, autorId = autor.id, conversaId = chat.id)
    }
}