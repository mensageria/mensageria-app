package com.tcc.mensageria.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe que cria o banco de dados
 */
public class MensageriaDbHelper extends SQLiteOpenHelper {

    // Versao do banco de dados, deve ser mudada toda vez que o schema for mudado
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "mensageria.db";

    public MensageriaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABELA_AUTOR = "CREATE TABLE " + MensageriaContract.Autores.NOME_TABELA + "(" +
                MensageriaContract.Autores._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MensageriaContract.Autores.COLUNA_NOME + " TEXT ," +
                MensageriaContract.Autores.COLUNA_EMAIL + " TEXT UNIQUE ," +
                MensageriaContract.Autores.COLUNA_EMAIL_CONFIRMADO + " INTEGER ," +
                MensageriaContract.Autores.COLUNA_ULTIMO_ACESSO + " INTEGER " +
                ");";

        final String SQL_CREATE_TABELA_CONVERSA = "CREATE TABLE " + MensageriaContract.Conversas.NOME_TABELA + "(" +
                MensageriaContract.Conversas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MensageriaContract.Conversas.COLUNA_TITULO + " TEXT," +
                MensageriaContract.Conversas.COLUNA_DATA_CRIACAO + " INTEGER," +
                MensageriaContract.Conversas.COLUNA_INTERATIVA + " INTEGER " +
                ");";

        final String SQL_CREATE_TABELA_MENSAGENS = "CREATE TABLE " + MensageriaContract.Mensagens.NOME_TABELA + " (" +
                MensageriaContract.Mensagens._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MensageriaContract.Mensagens.COLUNA_CONTEUDO + " TEXT ," +
                MensageriaContract.Mensagens.COLUNA_DATA_ENVIO + " INTEGER," +
                MensageriaContract.Mensagens.COLUNA_FK_AUTOR + " INTEGER ," +
                MensageriaContract.Mensagens.COLUNA_FK_CONVERSA + " INTEGER ," +

                " FOREIGN KEY (" + MensageriaContract.Mensagens.COLUNA_FK_AUTOR + ") REFERENCES " +
                MensageriaContract.Autores.NOME_TABELA + " (" + MensageriaContract.Autores._ID + ") " +
                " FOREIGN KEY (" + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA + ") REFERENCES " +
                MensageriaContract.Conversas.NOME_TABELA + " (" + MensageriaContract.Conversas._ID + ") " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABELA_AUTOR);
        sqLiteDatabase.execSQL(SQL_CREATE_TABELA_CONVERSA);
        sqLiteDatabase.execSQL(SQL_CREATE_TABELA_MENSAGENS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }
}
