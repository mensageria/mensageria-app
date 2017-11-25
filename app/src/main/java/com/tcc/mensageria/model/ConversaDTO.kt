package com.tcc.mensageria.model

import java.util.*


class ConversaDTO(
        var id: Long? = null,
        var titulo: String? = null,
        var autor: String? = null,
        var conteudo: String? = null,
        var estado: Char? = 'R',
        var dataEnvio: Date? = null
) {
}