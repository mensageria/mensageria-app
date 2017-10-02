package com.tcc.mensageria.service

import com.tcc.mensageria.network.RestRepository
import com.tcc.mensageria.viewmodel.ListaConversasViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(RetrofitModule::class, DatabaseModule::class))
interface MensageriaComponent {
    fun makeRestRepository(): RestRepository

    fun inject(conversasViewModel: ListaConversasViewModel): Unit
}