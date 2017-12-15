package com.tcc.mensageria.utils

import android.content.Context
import com.tcc.mensageria.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utility {
    fun getDataFormatada(dataMs: Long, context: Context): String {
        val mensagem: String
        val data = Calendar.getInstance()
        val diff = data.timeInMillis - dataMs
        val dias = TimeUnit.MILLISECONDS.toDays(diff)

        data.timeInMillis = dataMs
        val locale = Locale.getDefault()

        if (dias < 1) {
            val formatter = SimpleDateFormat("kk:mm")
            mensagem = formatter.format(data.time)
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
}

fun Date.formatHour(): String {
    val dateformat = SimpleDateFormat("HH:mm")
    return dateformat.format(this)
}