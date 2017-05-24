package com.tcc.mensageria.view.activity

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.tcc.mensageria.R
import com.tcc.mensageria.view.fragment.ConversaFragment

/**
 * View de detalhes de uma mensagem
 */
class ConversaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversa)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val arguments = Bundle()
            arguments.putInt(ConversaFragment.BUNDLE_ID_CONVERSA, intent.getIntExtra(ConversaFragment.BUNDLE_ID_CONVERSA, 0))

            val fragment = ConversaFragment()
            fragment.arguments = arguments

            supportFragmentManager.beginTransaction()
                    .add(R.id.container_conversa, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
