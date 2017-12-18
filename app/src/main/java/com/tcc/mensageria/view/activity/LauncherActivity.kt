package com.tcc.mensageria.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivityForResult(intent, SIGN_IN_CODE)
        super.onCreate(savedInstanceState)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {

        val SIGN_IN_CODE = 0
    }
}
