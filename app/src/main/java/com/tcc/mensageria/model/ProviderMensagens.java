
package com.tcc.mensageria.model;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ProviderMensagens extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MensageriaDbHelper mDBHelper;

    static final int MENSAGEM = 100;
    static final int MENSAGEM_COM_REMETENTE = 101;
    static final int REMETENTE = 200;

    private static final SQLiteQueryBuilder sMensagemComRemetenteQB;

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

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MensageriaContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
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
                throw new UnsupportedOperationException("Unknown uri: " + uri);
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
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MENSAGEM: {
                long _id = db.insert(MensageriaContract.Mensagens.NOME_TABELA, null, values);
                if (_id > 0)
                    returnUri = MensageriaContract.Mensagens.buildMensagemUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
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
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MENSAGEM:
                rowsDeleted = db.delete(
                        MensageriaContract.Mensagens.NOME_TABELA, selection, selectionArgs);
                break;
            case REMETENTE:
                rowsDeleted = db.delete(
                        MensageriaContract.Remetentes.NOME_TABELA, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MENSAGEM:
                rowsUpdated = db.update(MensageriaContract.Mensagens.NOME_TABELA, values, selection,
                        selectionArgs);
                break;
            case REMETENTE:
                rowsUpdated = db.update(MensageriaContract.Remetentes.NOME_TABELA, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
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