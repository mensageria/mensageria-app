package com.tcc.mensageria.di

import android.content.Context
import android.preference.PreferenceManager
import com.tcc.mensageria.R
import dagger.Module
import dagger.Provides
import okhttp3.WebSocket
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.client.StompClient
import javax.inject.Singleton


@Module
class StompModule(private val context: Context) {

    //TODO trocar quando remover a configuração de url manual
    @Singleton
    @Provides
    fun provideStomp(): StompClient {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val endereco = sharedPref.getString(context.getString(R.string.pref_endereco_key),
                context.getString(R.string.endereco_default))

        return Stomp.over(WebSocket::class.java, "ws://$endereco/mensageria/websocket")
    }
}