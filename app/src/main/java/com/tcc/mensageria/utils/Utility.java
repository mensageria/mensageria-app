package com.tcc.mensageria.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.tcc.mensageria.R;
import com.tcc.mensageria.model.BDUtil;
import com.tcc.mensageria.model.Mensagem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Classe de utilidades
 */
public class Utility {

    public static String getDataFormatada(long dataMs, Context context) {
        String mensagem;
        Calendar data = Calendar.getInstance();
        long diff = data.getTimeInMillis() - dataMs;
        long dias = TimeUnit.MILLISECONDS.toDays(diff);

        data.setTimeInMillis(dataMs);
        Locale locale = Locale.getDefault();

        if (dias < 1) {
            mensagem = data.get(Calendar.HOUR_OF_DAY) + ":" + data.get(Calendar.MINUTE);
        } else if (dias < 2) {
            mensagem = context.getString(R.string.ontem);
        } else if (dias < 7) {
            mensagem = new SimpleDateFormat("EEE",locale).format(data.getTime());
        }else if(dias < 365){
            mensagem = new SimpleDateFormat("dd MMM",locale).format(data.getTime());
        }
        else {
            mensagem = new SimpleDateFormat("dd/MM/yyyy",locale).format(data.getTime());
        }
        return mensagem;
    }


    public static void addJSONNoBanco(String JSON, Context context) {
        if (JSON == null) {
            return;
        }
        BDUtil bdUtil = new BDUtil(context);
        Gson gson = new Gson();
        Mensagem[] arrayMensagens = gson.fromJson(JSON,Mensagem[].class);
        bdUtil.addListaMensagem(arrayMensagens);
    }

}
