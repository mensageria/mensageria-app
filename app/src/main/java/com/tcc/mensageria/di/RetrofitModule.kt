package com.tcc.mensageria.di

import android.content.Context
import android.preference.PreferenceManager
import com.tcc.mensageria.R
import com.tcc.mensageria.network.MensagensService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule(private val context: Context) {

    //TODO trocar quando remover a configuração de url manual
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val endereco = sharedPref.getString(context.getString(R.string.pref_endereco_key),
                context.getString(R.string.endereco_default))

        return Retrofit.Builder()
                .baseUrl("http://" + endereco)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    fun provideMensagemService(retrofit: Retrofit): MensagensService {
        return retrofit.create(MensagensService::class.java)
    }
}