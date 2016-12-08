package com.tcc.mensageria.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tcc.mensageria.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexaoRest {
    private final String TAG = ConexaoRest.class.getSimpleName();

    /**
     * metodo para pegar os dados json do servidor
     */
    public String getJSON(Context context) {
        HttpURLConnection conexao = null;
        BufferedReader leitor = null;
        String JsonString = null;

        try {
            String URL_BASE = "http://";
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String endereco = sharedPref.getString(context.getString(R.string.pref_endereco_key)
                    , context.getString(R.string.endereco_default));
            URL_BASE += endereco;
            URL url = new URL(URL_BASE);

            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.connect();

            InputStream inputStream = conexao.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }
            leitor = new BufferedReader(new InputStreamReader(inputStream));

            String linha;
            while ((linha = leitor.readLine()) != null) {
                buffer.append(linha + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            JsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Erro de entrada", e);
        } finally {
            if (conexao != null) {
                conexao.disconnect();
            }
            if (leitor != null) {
                try {
                    leitor.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Erro ao fechar", e);
                }
            }
        }
        return JsonString;
    }
}
