package com.tcc.mensageria.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.tcc.mensageria.BuildConfig
import com.tcc.mensageria.model.Autor
import com.tcc.mensageria.model.Dispositivo
import com.tcc.mensageria.network.RestRepository
import javax.inject.Inject

class SignInViewModel : ViewModel() {

    @Inject
    lateinit var restRepository: RestRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences.Editor

    fun registrar(user: GoogleSignInAccount, sucesso: () -> Unit) {
        val nome = Build.MANUFACTURER + " " + Build.MODEL
        val autor = Autor(nome = user.displayName, email = user.email)
        val dispositivo = Dispositivo(nome, autor)
        restRepository.registrar(dispositivo, {
            Log.d(this::class.simpleName, "SUCESSO " + it.toString())
            sharedPreferences.putLong(BuildConfig.autor_key, it.proprietario.id!!)
            sharedPreferences.commit()
            sucesso()
        }, {
            Log.d(this::class.simpleName, "ERRO " + it.toString())
        })
    }

}