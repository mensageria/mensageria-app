package com.tcc.mensageria.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.tcc.mensageria.R

/**
 * View principal
 */
class MainActivity : AppCompatActivity() {

    internal val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Log.d(TAG, "onCreate: " + FirebaseAuth.getInstance().currentUser!!.email!!)
    }

    //TODO verificar se o google play services existe no onresume e no oncreate
    //    @Override
    //    protected void onResume() {
    //        int codigo = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
    //        if(codigo == ConnectionResult.SUCCESS){
    //            super.onResume();
    //        }
    //        else {
    //            GoogleApiAvailability.getInstance().getErrorDialog(getParent(),codigo,startActivityForResult(codigo);)
    //        }
    //    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (id) {
            R.id.action_settings -> {
                startActivity(Intent(this, PreferenciasActivity::class.java))
                return true
            }

            R.id.action_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

