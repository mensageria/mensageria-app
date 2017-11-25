package com.tcc.mensageria.network

import com.tcc.mensageria.model.Dispositivo
import com.tcc.mensageria.model.MensagemPOJO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface MensagensService {

    @GET("api/mensagens")
    fun getMensagens(): Call<List<MensagemPOJO>>

    @POST("api/registrar")
    fun registrar(@Body dispositivo: Dispositivo): Call<Dispositivo>
}