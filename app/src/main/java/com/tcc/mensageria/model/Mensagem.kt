package com.tcc.mensageria.model

data class Mensagem(var id: Long?,
                    var conteudo: String,
                    var dataEnvio: Long,
                    var autor: Autor,
                    var chat: Conversa) {


}
