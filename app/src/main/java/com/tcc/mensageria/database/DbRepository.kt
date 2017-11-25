package com.tcc.mensageria.database

import com.tcc.mensageria.model.MensagemPOJO
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class DbRepository @Inject constructor(private val mensagemDao: MensagemDao,
                                       private val conversaDao: ConversaDao, private val autorDao: AutorDao) {


    private fun inserirMensagemPOJO(mensagemPOJO: MensagemPOJO) {
        autorDao.inserir(mensagemPOJO.autor)
        conversaDao.inserir(mensagemPOJO.chat)
        mensagemDao.inserir(mensagemPOJO.getMensagem())
    }

    fun salvarMensagem(mensagemPOJO: MensagemPOJO) {
        launch(CommonPool) {
            inserirMensagemPOJO(mensagemPOJO)
        }
    }

    fun salvarMensagem(mensagemPOJOList: List<MensagemPOJO>) {
        launch(CommonPool) {
            for (mensagem in mensagemPOJOList) {
                inserirMensagemPOJO(mensagem)
            }
        }
    }
}
