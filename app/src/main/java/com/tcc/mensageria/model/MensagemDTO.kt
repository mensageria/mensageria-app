package com.tcc.mensageria.model

import java.util.*

class MensagemDTO(var conteudo: String? = null,
                  var dataEnvio: Date? = null,
                  var nomeAutor: String? = null,
                  var estado: Char? = 'E'
) {

}