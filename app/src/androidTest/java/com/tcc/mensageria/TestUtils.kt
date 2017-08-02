package com.tcc.mensageria

import com.tcc.mensageria.model.Autor
import com.tcc.mensageria.model.Conversa
import com.tcc.mensageria.model.Mensagem
import java.util.*

class TestUtils {
    companion object factory {
        fun criarAutor(tipo: Int?): Autor {
            return Autor(1, "email@email.com", "renan", Date())
        }

        fun criarConversa(tipo: Int?): Conversa {
            return Conversa(1, true, "conversa")
        }

        fun criarMensagem(tipo: Int?): Mensagem {
            when (tipo) {
                1 -> return Mensagem(1, "ola", Date(), true, true, 1, 1)
                else -> return Mensagem(2, "ola 2", Date(), null, null, 1, 1)
            }
        }
    }
}
