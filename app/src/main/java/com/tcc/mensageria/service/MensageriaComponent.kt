package com.tcc.mensageria.service

import com.tcc.mensageria.network.RestRepository
import com.tcc.mensageria.presenter.ConversasViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(RetrofitModule::class, DatabaseModule::class))
interface MensageriaComponent {
    fun makeRestRepository(): RestRepository

    fun inject(conversasViewModel: ConversasViewModel): Unit
}