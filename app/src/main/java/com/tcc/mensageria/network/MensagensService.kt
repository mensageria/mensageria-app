package com.tcc.mensageria.network

import com.tcc.mensageria.model.MensagemPOJO
import retrofit2.Call
import retrofit2.http.GET


interface MensagensService {

    @GET("/api/mensagens")
    fun getMensagens(): Call<List<MensagemPOJO>>
}