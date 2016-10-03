package com.tcc.mensageria.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MensageriaDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "mensageria.db";

    public MensageriaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABELA_REMETENTE = "CREATE TABLE " + MensageriaContract.Remetentes.NOME_TABELA + "(" +
                MensageriaContract.Remetentes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MensageriaContract.Remetentes.COLUNA_NOME + " TEXT NOT NULL," +
                MensageriaContract.Remetentes.COLUNA_EMAIL + " TEXT UNIQUE NOT NULL" +
                ");";


        final String SQL_CREATE_TABELA_MENSAGENS = "CREATE TABLE " + MensageriaContract.Mensagens.NOME_TABELA + " (" +
                MensageriaContract.Mensagens._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MensageriaContract.Mensagens.COLUNA_TITULO + " TEXT NOT NULL," +
                MensageriaContract.Mensagens.COLUNA_CONTEUDO + " TEXT NOT NULL," +
                MensageriaContract.Mensagens.COLUNA_FAVORITO + " INTEGER ," +
                MensageriaContract.Mensagens.COLUNA_FK_REMETENTE + " INTEGER NOT NULL," +

                " FOREIGN KEY (" + MensageriaContract.Mensagens.COLUNA_FK_REMETENTE + ") REFERENCES " +
                MensageriaContract.Remetentes.NOME_TABELA + " (" + MensageriaContract.Remetentes._ID + ") " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABELA_REMETENTE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABELA_MENSAGENS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        //TODO modificar para que nao drope a tabela com o upgrade
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MensageriaContract.Remetentes.NOME_TABELA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MensageriaContract.Mensagens.NOME_TABELA);
        onCreate(sqLiteDatabase);
    }
}
