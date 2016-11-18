package com.tcc.mensageria.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tcc.mensageria.R;

/**
 * View de detalhes de uma mensagem
 */
public class DetalhesMensagemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_mensagem);
    }
}
