package com.tcc.mensageria.network

import android.support.annotation.WorkerThread
import com.tcc.mensageria.model.MensagemPOJO
import com.tcc.mensageria.model.database.MensagemDao
import com.tcc.mensageria.service.MensagensService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class RestRepository @Inject constructor(
        private val mensagensService: MensagensService, private val mensagemDao: MensagemDao) {

    fun getMensagens(sucesso: (dados: Response<List<MensagemPOJO>>) -> Unit,
                     falha: (t: Throwable) -> Unit) {
        mensagensService.getMensagens().enqueue(object : Callback<List<MensagemPOJO>> {

            override fun onResponse(call: Call<List<MensagemPOJO>>?,
                                    response: Response<List<MensagemPOJO>>) {
                salvarMensagens(response.body())
                sucesso(response)
            }

            override fun onFailure(call: Call<List<MensagemPOJO>>, t: Throwable) {
                falha(t)
            }
        })
    }

    @WorkerThread
    fun salvarMensagens(listaMensagens: List<MensagemPOJO>) {
        for (mensagemPOJO in listaMensagens) {
            mensagemDao.inserir(mensagemPOJO.getMensagem())
        }
    }
}
