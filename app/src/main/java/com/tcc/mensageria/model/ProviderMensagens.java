
package com.tcc.mensageria.model;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Classe que lida com os dados do aplicativo
 */
public class ProviderMensagens extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MensageriaDbHelper mDBHelper;

    static final int MENSAGEM = 100;
    static final int MENSAGEM_COM_AUTOR = 101;
    static final int AUTOR = 200;
    static final int CONVERSA = 300;
    static final int CONVERSA_COM_AUTOR_E_MENSAGEM = 301;

    private static final SQLiteQueryBuilder sMensagemComAutorQB;
    private static final SQLiteQueryBuilder sConversacomAutorEMensagemQB;

    // query que faz o join da tabela mensagem com a tabela autores usando o id do autor
    static {
        sMensagemComAutorQB = new SQLiteQueryBuilder();


        sMensagemComAutorQB.setTables(
                MensageriaContract.Mensagens.NOME_TABELA + " INNER JOIN " +
                        MensageriaContract.Autores.NOME_TABELA +
                        " ON " + MensageriaContract.Mensagens.NOME_TABELA +
                        "." + MensageriaContract.Mensagens.COLUNA_FK_AUTOR +
                        " = " + MensageriaContract.Autores.NOME_TABELA +
                        "." + MensageriaContract.Autores._ID);
    }

    // query que faz o join da tabela mensagem com a tabela autorers e conversas
    static {
        sConversacomAutorEMensagemQB = new SQLiteQueryBuilder();

        // select * from conversas, mensagens, autores where conversas._id = mensagens.fk_conversa group by mensagens.fk_conversa
        sConversacomAutorEMensagemQB.setTables(
                MensageriaContract.Conversas.NOME_TABELA + ", " +
                        MensageriaContract.Autores.NOME_TABELA + ", " +
                        MensageriaContract.Mensagens.NOME_TABELA +
                        " WHERE " +
                        MensageriaContract.Conversas.NOME_TABELA + "." + MensageriaContract.Conversas._ID +
                        " = " +
                        MensageriaContract.Mensagens.NOME_TABELA + "." + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA +
                        " GROUP BY " +
                        MensageriaContract.Mensagens.NOME_TABELA + "." + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA);
    }

    /**
     * Cria um validador com as uris usadas pelo content provider para acessar os dados
     *
     * @return um validador de uris com as uris criadas
     */
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MensageriaContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MensageriaContract.PATH_MENSAGEM, MENSAGEM);
        matcher.addURI(authority, MensageriaContract.PATH_MENSAGEM + "/" +
                MensageriaContract.PATH_AUTOR, MENSAGEM_COM_AUTOR);

        matcher.addURI(authority, MensageriaContract.PATH_CONVERSA, CONVERSA);
        matcher.addURI(authority, MensageriaContract.PATH_CONVERSA + "/" +
                MensageriaContract.PATH_AUTOR + "/" + MensageriaContract.PATH_MENSAGEM, CONVERSA_COM_AUTOR_E_MENSAGEM);

        matcher.addURI(authority, MensageriaContract.PATH_AUTOR, AUTOR);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new MensageriaDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MENSAGEM:
                return MensageriaContract.Mensagens.CONTENT_TYPE;
            case MENSAGEM_COM_AUTOR:
                return MensageriaContract.Mensagens.CONTENT_TYPE_COM_AUTOR;
            case CONVERSA_COM_AUTOR_E_MENSAGEM:
                return MensageriaContract.Mensagens.CONTENT_TYPE_COM_AUTOR_E_CONVERSA;
            case AUTOR:
                return MensageriaContract.Autores.CONTENT_TYPE;
            case CONVERSA:
                return MensageriaContract.Conversas.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("uri Desconhecida: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "mensagem"
            case MENSAGEM: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        MensageriaContract.Mensagens.NOME_TABELA,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "mensagem/autor"
            case MENSAGEM_COM_AUTOR: {
                retCursor = sMensagemComAutorQB.query(mDBHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "mensagem/autor/conversa"
            case CONVERSA_COM_AUTOR_E_MENSAGEM: {
                retCursor = sConversacomAutorEMensagemQB.query(mDBHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "autor"
            case AUTOR: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        MensageriaContract.Autores.NOME_TABELA,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "conversa"
            case CONVERSA: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        MensageriaContract.Conversas.NOME_TABELA,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            // "mensagem"
            case MENSAGEM: {
                long _id = db.insert(MensageriaContract.Mensagens.NOME_TABELA, null, values);
                if (_id > 0)
                    returnUri = MensageriaContract.Mensagens.buildMensagemUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            // "autor"
            case AUTOR: {
                long _id = db.insert(MensageriaContract.Autores.NOME_TABELA, null, values);
                if (_id > 0)
                    returnUri = MensageriaContract.Autores.buildAutorUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            // "conversa"
            case CONVERSA: {
                long _id = db.insert(MensageriaContract.Conversas.NOME_TABELA, null, values);
                if (_id > 0)
                    returnUri = MensageriaContract.Conversas.buildConversaUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        int qtdDeletados;

        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            // "mensagem"
            case MENSAGEM:
                qtdDeletados = db.delete(
                        MensageriaContract.Mensagens.NOME_TABELA, selection, selectionArgs);
                break;
            // "autor"
            case AUTOR:
                qtdDeletados = db.delete(
                        MensageriaContract.Autores.NOME_TABELA, selection, selectionArgs);
                break;
            // "conversa"
            case CONVERSA:
                qtdDeletados = db.delete(
                        MensageriaContract.Conversas.NOME_TABELA, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (qtdDeletados != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return qtdDeletados;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int qtdAtualizados;

        switch (match) {
            // "mensagem"
            case MENSAGEM:
                qtdAtualizados = db.update(MensageriaContract.Mensagens.NOME_TABELA, values, selection,
                        selectionArgs);
                break;
            // "autor"
            case AUTOR:
                qtdAtualizados = db.update(MensageriaContract.Autores.NOME_TABELA, values, selection,
                        selectionArgs);
                break;
            // "conversa"
            case CONVERSA:
                qtdAtualizados = db.update(MensageriaContract.Conversas.NOME_TABELA, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Uri desconhecida: " + uri);
        }
        if (qtdAtualizados != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return qtdAtualizados;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            // "mensagem"
            case MENSAGEM:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MensageriaContract.Mensagens.NOME_TABELA, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }
}