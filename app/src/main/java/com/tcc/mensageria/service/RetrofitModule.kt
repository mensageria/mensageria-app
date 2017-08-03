package com.tcc.mensageria.service

import android.content.Context
import com.google.gson.GsonBuilder
import com.tcc.mensageria.R
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitModule(private val context: Context) {

    //TODO trocar quando remover a configuração de url manual
    @Provides fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()

        return Retrofit.Builder()
                .baseUrl("http://" + context.getString(R.string.endereco_default))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides fun provideContext(): Context {
        return context
    }

    @Provides fun provideMensagemService(retrofit: Retrofit): MensagensService {
        return retrofit.create(MensagensService::class.java)
    }
}