package com.tcc.mensageria.model

import java.util.*


data class MensagemPOJO(
        var id: Long? = null,
        var conteudo: String? = null,
        var dataEnvio: Date? = null,
        var chat: Conversa,
        var autor: Autor
) {

}