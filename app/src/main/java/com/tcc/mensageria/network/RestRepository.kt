package com.tcc.mensageria.network

import com.tcc.mensageria.model.Dispositivo
import com.tcc.mensageria.model.MensagemPOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class RestRepository @Inject constructor(
        private val mensagensService: MensagensService) {

    fun getMensagens(sucesso: (dados: List<MensagemPOJO>) -> Unit,
                     falha: (t: Throwable) -> Unit) {
        mensagensService.getMensagens().enqueue(object : Callback<List<MensagemPOJO>> {

            override fun onResponse(call: Call<List<MensagemPOJO>>,
                                    response: Response<List<MensagemPOJO>>) {
                response.body()?.let { sucesso(it) }
            }

            override fun onFailure(call: Call<List<MensagemPOJO>>, t: Throwable) {
                falha(t)
            }
        })
    }

    fun registrar(dispositivo: Dispositivo,
                  sucesso: (dados: Dispositivo) -> Unit,
                  falha: (t: Throwable) -> Unit) {
        mensagensService.registrar(dispositivo).enqueue(object : Callback<Dispositivo> {

            override fun onResponse(call: Call<Dispositivo>,
                                    response: Response<Dispositivo>) {
                response.body()?.let { sucesso(it) }
            }

            override fun onFailure(call: Call<Dispositivo>, t: Throwable) {
                falha(t)
            }
        })
    }
}
