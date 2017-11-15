package com.tcc.mensageria.di

import com.tcc.mensageria.network.RestRepository
import com.tcc.mensageria.viewmodel.ConversaViewModel
import com.tcc.mensageria.viewmodel.ListaConversasViewModel
import dagger.Component
import javax.inject.Singleton

//TODO usar somente os modulos necessarios
@Singleton
@Component(modules = arrayOf(RetrofitModule::class, DatabaseModule::class, StompModule::class))
interface MensageriaComponent {
    fun makeRestRepository(): RestRepository

    fun inject(listaConversasViewModel: ListaConversasViewModel)

    fun inject(conversasViewModel: ConversaViewModel)
}