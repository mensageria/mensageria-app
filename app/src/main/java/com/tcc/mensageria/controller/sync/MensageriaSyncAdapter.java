package com.tcc.mensageria.controller.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.tcc.mensageria.R;
import com.tcc.mensageria.model.MensageriaContract;
import com.tcc.mensageria.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * classe para lidar com as conexoes com o servidor utilizando um sync adapter
 */
public class MensageriaSyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = MensageriaSyncAdapter.class.getSimpleName();
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public MensageriaSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public MensageriaSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    // faz a sincronização
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: ");
        getJSON();
    }

    /**
     * metodo para pegar os dados json do servidor
     */
    public void getJSON() {
        HttpURLConnection conexao = null;
        BufferedReader leitor = null;
        String JsonString = null;

        try {
            String URL_BASE = "http://";
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String endereco = sharedPref.getString(getContext().getString(R.string.pref_endereco_key)
                    , getContext().getString(R.string.endereco_default));
            URL_BASE += endereco;
            URL url = new URL(URL_BASE);

            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.connect();


            InputStream inputStream = conexao.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return;
            }
            leitor = new BufferedReader(new InputStreamReader(inputStream));

            String linha;
            while ((linha = leitor.readLine()) != null) {
                buffer.append(linha + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }

            JsonString = buffer.toString();

            parseJson(JsonString);
        } catch (IOException e) {
            Log.e(TAG, "Erro ", e);
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

    }

    /**
     * Metodo para formatar a string json recebida
     *
     * @param JSON string para ser formatada
     */
    public void parseJson(String JSON) {
        if (JSON == null) {
            return;
        }


        Vector<ContentValues> listaMensagens = new Vector<>();

        try {
            JSONArray jsonArray = new JSONArray(JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject objeto = jsonArray.getJSONObject(i);
                long id = objeto.getLong("id");
                String conteudo = objeto.getString("conteudo");
                String titulo = objeto.getString("titulo");
                //TODO fazer o favorito
                boolean favorito = false;

//                JSONObject remetente = objeto.getJSONObject("remetente");
//                String nome = remetente.getString("nome");
//                String email = remetente.getString("contato");
                String nome = "Unigranrio";
                String email = "email@email.com";
                long idRemetente = addRemetente(nome, email);

                ContentValues contentValues = new ContentValues();
                contentValues.put(MensageriaContract.Mensagens._ID, id);
                contentValues.put(MensageriaContract.Mensagens.COLUNA_CONTEUDO, conteudo);
                contentValues.put(MensageriaContract.Mensagens.COLUNA_TITULO, titulo);
                contentValues.put(MensageriaContract.Mensagens.COLUNA_FAVORITO, favorito ? 1 : 0);
                contentValues.put(MensageriaContract.Mensagens.COLUNA_FK_REMETENTE, idRemetente);
                listaMensagens.add(contentValues);
            }
            addMensagem(listaMensagens);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para adicionar remetentes via content provider
     * @param nome nome do remetente
     * @param email email do remetente
     * @return o id do remetente inserido
     */
    private long addRemetente(String nome, String email) {
        long idRemetente;

        Cursor cursor = getContext().getContentResolver().query(
                MensageriaContract.Remetentes.CONTENT_URI,
                new String[]{MensageriaContract.Remetentes._ID},
                MensageriaContract.Remetentes.COLUNA_EMAIL + " = ?",
                new String[]{email},
                null);

        if (cursor.moveToFirst()) {
            int indexremetente = cursor.getColumnIndex(MensageriaContract.Remetentes._ID);
            idRemetente = cursor.getLong(indexremetente);
        } else {
            ContentValues valores = new ContentValues();
            valores.put(MensageriaContract.Remetentes.COLUNA_NOME, nome);
            valores.put(MensageriaContract.Remetentes.COLUNA_EMAIL, email);

            Uri uriInsercao = getContext().getContentResolver().insert(
                    MensageriaContract.Remetentes.CONTENT_URI,
                    valores
            );

            idRemetente = ContentUris.parseId(uriInsercao);
        }

        cursor.close();
        return idRemetente;
    }

    /**
     * Metodo para adicionar mensagens via content provider
     * @param listaMensagens lista de mensagens para serem adicionadas no banco
     */
    private void addMensagem(Vector<ContentValues> listaMensagens) {
        if (listaMensagens.size() > 0) {
            ContentValues[] cvArray = new ContentValues[listaMensagens.size()];
            listaMensagens.toArray(cvArray);
            int qtdMensagens = getContext().getContentResolver().bulkInsert(MensageriaContract.Mensagens.CONTENT_URI,
                    cvArray);
            if (qtdMensagens > 0) {
                notificar();
            }
        }
    }

    /**
     * Metodo para notificar o usuario quando uma nova mensagem chegar
     */
    //TODO ver numero de mensagens nao lidas
    private void notificar() {
        int mId = 1;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Novas Mensagens Recebidas!")
                        //.setContentText(qtdMensagens + " nova(s) mensagem(s)")
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getContext(), MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        notificationManager.notify(mId, mBuilder.build());
    }

    /**
     * Criar uma conta para o sync adapter
     *
     * @param context O contexto da aplicação
     */
    public static Account GetSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        }
        return newAccount;

    }

    /**
     * Metodo para sincronizar o sync adapter
     *
     * @param context O contexto usado para fazer a sincronização
     */
    public static void syncImmediately(Context context) {
        ContentResolver.requestSync(GetSyncAccount(context),
                context.getString(R.string.content_authority), Bundle.EMPTY);
    }

}
