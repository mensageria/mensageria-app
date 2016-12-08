package com.tcc.mensageria.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.tcc.mensageria.R;
import com.tcc.mensageria.network.ConexaoRest;
import com.tcc.mensageria.utils.Utility;
import com.tcc.mensageria.view.activity.MainActivity;

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
        ConexaoRest conexaoRest  = new ConexaoRest();
        String JsonMensagens = conexaoRest.getJSON(getContext());
        Utility.addJSONNoBanco(JsonMensagens,getContext());
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
