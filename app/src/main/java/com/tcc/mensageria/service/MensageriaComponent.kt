package com.tcc.mensageria.service

import com.tcc.mensageria.network.RestRepository
import dagger.Component


@Component(modules = arrayOf(RetrofitModule::class))
interface MensageriaComponent {
    fun makeRestRepository(): RestRepository
}