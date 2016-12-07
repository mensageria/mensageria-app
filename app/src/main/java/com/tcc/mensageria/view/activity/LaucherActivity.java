package com.tcc.mensageria.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LaucherActivity extends AppCompatActivity {

    public static final int SIGN_IN_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, SIGN_IN_CODE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
