package com.tcc.mensageria.network

import com.tcc.mensageria.database.AutorDao
import com.tcc.mensageria.database.ConversaDao
import com.tcc.mensageria.database.MensagemDao
import com.tcc.mensageria.model.MensagemPOJO
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class RestRepository @Inject constructor(
        private val mensagensService: MensagensService, private val mensagemDao: MensagemDao,
        private val conversaDao: ConversaDao, private val autorDao: AutorDao) {

    fun getMensagens(sucesso: (dados: List<MensagemPOJO>) -> Unit,
                     falha: (t: Throwable) -> Unit) {
        mensagensService.getMensagens().enqueue(object : Callback<List<MensagemPOJO>> {

            override fun onResponse(call: Call<List<MensagemPOJO>>,
                                    response: Response<List<MensagemPOJO>>) {
                response.body()?.let { salvarMensagens(it) }
                response.body()?.let { sucesso(it) }
            }

            override fun onFailure(call: Call<List<MensagemPOJO>>, t: Throwable) {
                falha(t)
            }
        })
    }

    fun salvarMensagens(listaMensagens: List<MensagemPOJO>) {
        launch(CommonPool) {
            for (mensagemPOJO in listaMensagens) {
                autorDao.inserir(mensagemPOJO.autor)
                conversaDao.inserir(mensagemPOJO.chat)
                mensagemDao.inserir(mensagemPOJO.getMensagem())
            }
        }
    }
}
