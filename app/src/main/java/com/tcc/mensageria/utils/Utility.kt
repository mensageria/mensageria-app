package com.tcc.mensageria.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.tcc.mensageria.R
import com.tcc.mensageria.model.BDUtil
import com.tcc.mensageria.model.Mensagem
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Classe de utilidades
 */
object Utility {

    @SuppressLint("WrongConstant")
    fun getDataFormatada(dataMs: Long, context: Context): String {
        val mensagem: String
        val data = Calendar.getInstance()
        val diff = data.timeInMillis - dataMs
        val dias = TimeUnit.MILLISECONDS.toDays(diff)

        data.timeInMillis = dataMs
        val locale = Locale.getDefault()

        if (dias < 1) {
            mensagem = data.get(Calendar.HOUR_OF_DAY).toString() + ":" + data.get(Calendar.MINUTE)
        } else if (dias < 2) {
            mensagem = context.getString(R.string.ontem)
        } else if (dias < 7) {
            mensagem = SimpleDateFormat("EEE", locale).format(data.time)
        } else if (dias < 365) {
            mensagem = SimpleDateFormat("dd MMM", locale).format(data.time)
        } else {
            mensagem = SimpleDateFormat("dd/MM/yyyy", locale).format(data.time)
        }
        return mensagem
    }


    fun addJSONNoBanco(JSON: String?, context: Context) {
        if (JSON == null) {
            return
        }
        val bdUtil = BDUtil(context)
        val gson = Gson()
        val arrayMensagens = gson.fromJson(JSON, Array<Mensagem>::class.java)
        bdUtil.addListaMensagem(arrayMensagens)
    }

}
