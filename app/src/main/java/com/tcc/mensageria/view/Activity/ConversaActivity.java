package com.tcc.mensageria.view.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tcc.mensageria.R;
import com.tcc.mensageria.view.fragment.ConversaFragment;

/**
 * View de detalhes de uma mensagem
 */
public class ConversaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(ConversaFragment.BUNDLE_ID_CONVERSA, getIntent().
                    getIntExtra(ConversaFragment.BUNDLE_ID_CONVERSA, 0));

            ConversaFragment fragment = new ConversaFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_conversa, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
