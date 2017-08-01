package com.tcc.mensageria.service

import com.tcc.mensageria.model.MensagemPOJO
import retrofit2.Call
import retrofit2.http.GET


interface MensagensService {

    @GET("/mensagens")
    fun getMensagens(): Call<List<MensagemPOJO>>
}