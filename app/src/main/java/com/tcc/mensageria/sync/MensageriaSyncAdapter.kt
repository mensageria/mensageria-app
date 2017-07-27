package com.tcc.mensageria.sync

import android.accounts.Account
import android.accounts.AccountManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import com.tcc.mensageria.R
import com.tcc.mensageria.network.ConexaoRest
import com.tcc.mensageria.view.activity.MainActivity

/**
 * classe para lidar com as conexoes com o servidor utilizando um sync adapter
 */
class MensageriaSyncAdapter : AbstractThreadedSyncAdapter {
    private val TAG = MensageriaSyncAdapter::class.java.simpleName
    // Global variables
    // Define a variable to contain a content resolver instance
    internal var mContentResolver: ContentResolver

    /**
     * Set up the sync adapter
     */
    constructor(context: Context, autoInitialize: Boolean) : super(context, autoInitialize) {
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.contentResolver
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    constructor(
            context: Context,
            autoInitialize: Boolean,
            allowParallelSyncs: Boolean) : super(context, autoInitialize, allowParallelSyncs) {
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.contentResolver
    }

    // faz a sincronização
    override fun onPerformSync(account: Account, extras: Bundle, authority: String,
                               provider: ContentProviderClient, syncResult: SyncResult) {
        Log.d(TAG, "onPerformSync: ")
        val conexaoRest = ConexaoRest()
        val JsonMensagens = conexaoRest.getJSON(context)
//        Utility.addJSONNoBanco(JsonMensagens, context)
    }

    /**
     * Metodo para notificar o usuario quando uma nova mensagem chegar
     */
    //TODO ver numero de mensagens nao lidas
    private fun notificar() {
        val mId = 1
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Novas Mensagens Recebidas!")
                //.setContentText(qtdMensagens + " nova(s) mensagem(s)")
                .setAutoCancel(true)

        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(context, MainActivity::class.java)

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        val stackBuilder = TaskStackBuilder.create(context)

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity::class.java)

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)

        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // mId allows you to update the notification later on.
        notificationManager.notify(mId, mBuilder.build())
    }

    companion object {

        /**
         * Criar uma conta para o sync adapter

         * @param context O contexto da aplicação
         */
        fun GetSyncAccount(context: Context): Account? {
            // Get an instance of the Android account manager
            val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

            // Create the account type and default account
            val newAccount = Account(
                    context.getString(R.string.app_name), context.getString(R.string.sync_account_type))

            // If the password doesn't exist, the account doesn't exist
            if (null == accountManager.getPassword(newAccount)) {

                /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
                if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                    return null
                }
                /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
                ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true)
            }
            return newAccount

        }

        /**
         * Metodo para sincronizar o sync adapter

         * @param context O contexto usado para fazer a sincronização
         */
        fun syncImmediately(context: Context) {
            ContentResolver.requestSync(GetSyncAccount(context),
                    context.getString(R.string.content_authority), Bundle.EMPTY)
        }
    }

}
