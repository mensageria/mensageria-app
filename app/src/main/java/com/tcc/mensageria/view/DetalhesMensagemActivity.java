package com.tcc.mensageria.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tcc.mensageria.R;

/**
 * View de detalhes de uma mensagem
 */
public class DetalhesMensagemActivity extends AppCompatActivity {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_MENSAGEM = "EXTRA_MENSAGEM";
    private static final String EXTRA_AUTOR = "EXTRA_AUTOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_mensagem);

        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        ((TextView)findViewById(R.id.detalhes_conteudo)).setText(extras.getString(EXTRA_MENSAGEM));
        ((TextView)findViewById(R.id.detalhes_autor)).setText(extras.getString(EXTRA_AUTOR));
    }
}
