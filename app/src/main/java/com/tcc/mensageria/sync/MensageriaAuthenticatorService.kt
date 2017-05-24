package com.tcc.mensageria.sync


import android.app.Service
import android.content.Intent
import android.os.IBinder

class MensageriaAuthenticatorService : Service() {

    // Instance field that stores the authenticator object
    private var mAuthenticator: MensageriaAuthenticator? = null

    override fun onCreate() {
        // Create a new authenticator object
        mAuthenticator = MensageriaAuthenticator(this)
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    override fun onBind(intent: Intent): IBinder? {
        return mAuthenticator!!.iBinder
    }

}
