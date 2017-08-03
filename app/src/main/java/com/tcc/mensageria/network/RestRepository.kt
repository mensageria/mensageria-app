package com.tcc.mensageria.network

import android.content.Context
import com.tcc.mensageria.model.MensagemPOJO
import com.tcc.mensageria.service.MensagensService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class RestRepository public @Inject constructor(
        private val context: Context, private val mensagensService: MensagensService) {

    fun getMensagens(sucesso: (dados: List<MensagemPOJO>) -> Unit,
                     falha: (t: Throwable) -> Unit) {
        mensagensService.getMensagens().enqueue(object : Callback<List<MensagemPOJO>> {
            override fun onResponse(call: Call<List<MensagemPOJO>>?, response: Response<List<MensagemPOJO>>?) {
                if (response != null) {
                    sucesso(response.body())
                }
            }

            override fun onFailure(call: Call<List<MensagemPOJO>>?, t: Throwable?) {
                if (t != null) {
                    falha(t)
                }
            }
        })
    }
}
