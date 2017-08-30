package com.tcc.mensageria.model

import java.util.*


class ConversaDTO(
        var id: Long? = null,
        var titulo: String? = null,
        var autor: String? = null,
        var conteudo: String? = null,
        var enviada: Boolean? = null,
        var recebida: Boolean? = null,
        var dataEnvio: Date? = null
) {
}