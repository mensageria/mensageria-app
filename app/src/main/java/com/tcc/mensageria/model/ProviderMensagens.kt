package com.tcc.mensageria.model

import android.annotation.TargetApi
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

/**
 * Classe que lida com os dados do aplicativo
 */
class ProviderMensagens : ContentProvider() {
    private var mDBHelper: MensageriaDbHelper? = null

    override fun onCreate(): Boolean {
        mDBHelper = MensageriaDbHelper(context)
        return true
    }

    override fun getType(uri: Uri): String? {

        val match = sUriMatcher.match(uri)

        when (match) {
            MENSAGEM -> return MensageriaContract.Mensagens.CONTENT_TYPE
            MENSAGEM_COM_AUTOR -> return MensageriaContract.Mensagens.CONTENT_TYPE_COM_AUTOR
            CONVERSA_COM_AUTOR_E_MENSAGEM -> return MensageriaContract.Mensagens.CONTENT_TYPE_COM_AUTOR_E_CONVERSA
            AUTOR -> return MensageriaContract.Autores.CONTENT_TYPE
            CONVERSA -> return MensageriaContract.Conversas.CONTENT_TYPE
            else -> throw UnsupportedOperationException("uri Desconhecida: " + uri)
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?,
                       sortOrder: String?): Cursor? {
        val retCursor: Cursor
        when (sUriMatcher.match(uri)) {
        // "mensagem"
            MENSAGEM -> {
                retCursor = mDBHelper!!.readableDatabase.query(
                        MensageriaContract.Mensagens.NOME_TABELA,
                        projection,
                        selection,
                        selectionArgs, null, null,
                        sortOrder
                )
            }
        // "mensagem/autor"
            MENSAGEM_COM_AUTOR -> {
                retCursor = sMensagemComAutorQB.query(mDBHelper!!.readableDatabase,
                        projection,
                        selection,
                        selectionArgs, null, null,
                        sortOrder
                )
            }

        // "mensagem/autor/conversa"
            CONVERSA_COM_AUTOR_E_MENSAGEM -> {
                retCursor = sConversacomAutorEMensagemQB.query(mDBHelper!!.readableDatabase,
                        projection,
                        selection,
                        selectionArgs, null, null,
                        sortOrder
                )
            }
        // "autor"
            AUTOR -> {
                retCursor = mDBHelper!!.readableDatabase.query(
                        MensageriaContract.Autores.NOME_TABELA,
                        projection,
                        selection,
                        selectionArgs, null, null,
                        sortOrder
                )
            }
        // "conversa"
            CONVERSA -> {
                retCursor = mDBHelper!!.readableDatabase.query(
                        MensageriaContract.Conversas.NOME_TABELA,
                        projection,
                        selection,
                        selectionArgs, null, null,
                        sortOrder
                )
            }

            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
        retCursor.setNotificationUri(context!!.contentResolver, uri)
        return retCursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = mDBHelper!!.writableDatabase
        val returnUri: Uri

        when (sUriMatcher.match(uri)) {
        // "mensagem"
            MENSAGEM -> {
                val _id = db.insert(MensageriaContract.Mensagens.NOME_TABELA, null, values)
                if (_id > 0)
                    returnUri = MensageriaContract.Mensagens.buildMensagemUri(_id)
                else
                    throw android.database.SQLException("Failed to insert row into " + uri)
            }
        // "autor"
            AUTOR -> {
                val _id = db.insert(MensageriaContract.Autores.NOME_TABELA, null, values)
                if (_id > 0)
                    returnUri = MensageriaContract.Autores.buildAutorUri(_id)
                else
                    throw android.database.SQLException("Failed to insert row into " + uri)
            }
        // "conversa"
            CONVERSA -> {
                val _id = db.insert(MensageriaContract.Conversas.NOME_TABELA, null, values)
                if (_id > 0)
                    returnUri = MensageriaContract.Conversas.buildConversaUri(_id)
                else
                    throw android.database.SQLException("Failed to insert row into " + uri)
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
        context!!.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        val db = mDBHelper!!.writableDatabase

        val qtdDeletados: Int

        if (null == selection) selection = "1"
        when (sUriMatcher.match(uri)) {
        // "mensagem"
            MENSAGEM -> qtdDeletados = db.delete(
                    MensageriaContract.Mensagens.NOME_TABELA, selection, selectionArgs)
        // "autor"
            AUTOR -> qtdDeletados = db.delete(
                    MensageriaContract.Autores.NOME_TABELA, selection, selectionArgs)
        // "conversa"
            CONVERSA -> qtdDeletados = db.delete(
                    MensageriaContract.Conversas.NOME_TABELA, selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        if (qtdDeletados != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return qtdDeletados
    }

    override fun update(
            uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = mDBHelper!!.writableDatabase
        val match = sUriMatcher.match(uri)
        val qtdAtualizados: Int

        when (match) {
        // "mensagem"
            MENSAGEM -> qtdAtualizados = db.update(MensageriaContract.Mensagens.NOME_TABELA, values, selection,
                    selectionArgs)
        // "autor"
            AUTOR -> qtdAtualizados = db.update(MensageriaContract.Autores.NOME_TABELA, values, selection,
                    selectionArgs)
        // "conversa"
            CONVERSA -> qtdAtualizados = db.update(MensageriaContract.Conversas.NOME_TABELA, values, selection,
                    selectionArgs)
            else -> throw UnsupportedOperationException("Uri desconhecida: " + uri)
        }
        if (qtdAtualizados != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return qtdAtualizados
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        val db = mDBHelper!!.writableDatabase

        when (sUriMatcher.match(uri)) {
        // "mensagem"
            MENSAGEM -> {
                db.beginTransaction()
                var returnCount = 0
                try {
                    for (value in values) {
                        val _id = db.insert(MensageriaContract.Mensagens.NOME_TABELA, null, value)
                        if (_id.toInt() != -1) {
                            returnCount++
                        }
                    }
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
                context!!.contentResolver.notifyChange(uri, null)
                return returnCount
            }
            else -> return super.bulkInsert(uri, values)
        }
    }

    @TargetApi(11)
    override fun shutdown() {
        mDBHelper!!.close()
        super.shutdown()
    }

    companion object {

        private val sUriMatcher = buildUriMatcher()

        internal val MENSAGEM = 100
        internal val MENSAGEM_COM_AUTOR = 101
        internal val AUTOR = 200
        internal val CONVERSA = 300
        internal val CONVERSA_COM_AUTOR_E_MENSAGEM = 301

        private val sMensagemComAutorQB: SQLiteQueryBuilder
        private val sConversacomAutorEMensagemQB: SQLiteQueryBuilder

        // query que faz o join da tabela mensagem com a tabela autores usando o id do autor
        init {
            sMensagemComAutorQB = SQLiteQueryBuilder()

            sMensagemComAutorQB.tables = MensageriaContract.Mensagens.NOME_TABELA + " INNER JOIN " +
                    MensageriaContract.Autores.NOME_TABELA +
                    " ON " + MensageriaContract.Mensagens.NOME_TABELA +
                    "." + MensageriaContract.Mensagens.COLUNA_FK_AUTOR +
                    " = " + MensageriaContract.Autores.NOME_TABELA +
                    "." + MensageriaContract.Autores._ID
        }

        // query que faz o join da tabela mensagem com a tabela autores e conversas
        init {
            sConversacomAutorEMensagemQB = SQLiteQueryBuilder()

            // select * from conversas, mensagens, autores where conversas._id = mensagens.fk_conversa group by mensagens.fk_conversa
            sConversacomAutorEMensagemQB.tables = MensageriaContract.Conversas.NOME_TABELA + ", " +
                    MensageriaContract.Autores.NOME_TABELA + ", " +
                    MensageriaContract.Mensagens.NOME_TABELA
        }

        /**
         * Cria um validador com as uris usadas pelo content provider para acessar os dados

         * @return um validador de uris com as uris criadas
         */
        internal fun buildUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = MensageriaContract.CONTENT_AUTHORITY

            matcher.addURI(authority, MensageriaContract.PATH_MENSAGEM, MENSAGEM)
            matcher.addURI(authority, MensageriaContract.PATH_MENSAGEM + "/" +
                    MensageriaContract.PATH_AUTOR, MENSAGEM_COM_AUTOR)

            matcher.addURI(authority, MensageriaContract.PATH_CONVERSA, CONVERSA)
            matcher.addURI(authority, MensageriaContract.PATH_CONVERSA + "/" +
                    MensageriaContract.PATH_AUTOR + "/" + MensageriaContract.PATH_MENSAGEM, CONVERSA_COM_AUTOR_E_MENSAGEM)

            matcher.addURI(authority, MensageriaContract.PATH_AUTOR, AUTOR)

            return matcher
        }
    }
}