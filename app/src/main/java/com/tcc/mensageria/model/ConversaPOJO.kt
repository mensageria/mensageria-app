package com.tcc.mensageria.model


class ConversaPOJO(var id: Long,
                   var interativa: Boolean,
                   var nome: String? = null,
                   var ultimaMensagem: MensagemPOJO) {
}