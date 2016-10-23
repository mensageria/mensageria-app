
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
    static final int MENSAGEM_COM_REMETENTE = 101;
    static final int REMETENTE = 200;

    private static final SQLiteQueryBuilder sMensagemComRemetenteQB;

    // query que faz o join da tabela mensagem com a tabela remetente usando o id do remetente
    static {
        sMensagemComRemetenteQB = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sMensagemComRemetenteQB.setTables(
                MensageriaContract.Mensagens.NOME_TABELA + " INNER JOIN " +
                        MensageriaContract.Remetentes.NOME_TABELA +
                        " ON " + MensageriaContract.Mensagens.NOME_TABELA +
                        "." + MensageriaContract.Mensagens.COLUNA_FK_REMETENTE +
                        " = " + MensageriaContract.Remetentes.NOME_TABELA +
                        "." + MensageriaContract.Remetentes._ID);
    }

    /**
     * Cria um validador com as uris usadas pelo content provider para acessar os dados
     * @return um validador de uris com as uris criadas
     */
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MensageriaContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MensageriaContract.PATH_MENSAGEM, MENSAGEM);
        matcher.addURI(authority, MensageriaContract.PATH_MENSAGEM + "/" + MensageriaContract.PATH_REMETENTE, MENSAGEM_COM_REMETENTE);
        matcher.addURI(authority, MensageriaContract.PATH_REMETENTE, REMETENTE);

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
            case MENSAGEM_COM_REMETENTE:
                return MensageriaContract.Mensagens.CONTENT_TYPE_COM_REMETENTE;
            case REMETENTE:
                return MensageriaContract.Remetentes.CONTENT_TYPE;
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
            // "mensagem/*"
            case MENSAGEM_COM_REMETENTE: {
                retCursor = sMensagemComRemetenteQB.query(mDBHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "remetente"
            case REMETENTE: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        MensageriaContract.Remetentes.NOME_TABELA,
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
            // "remetente"
            case REMETENTE: {
                long _id = db.insert(MensageriaContract.Remetentes.NOME_TABELA, null, values);
                if (_id > 0)
                    returnUri = MensageriaContract.Mensagens.buildMensagemUri(_id);
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
            // "remetente"
            case REMETENTE:
                qtdDeletados = db.delete(
                        MensageriaContract.Remetentes.NOME_TABELA, selection, selectionArgs);
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
            // "remetente"
            case REMETENTE:
                qtdAtualizados = db.update(MensageriaContract.Remetentes.NOME_TABELA, values, selection,
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