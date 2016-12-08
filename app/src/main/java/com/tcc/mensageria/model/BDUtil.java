package com.tcc.mensageria.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Vector;

import static android.content.ContentUris.parseId;

public class BDUtil {

    private Context mContext;

    public BDUtil(Context mContext) {
        this.mContext = mContext;
    }

    public long addAutor(Autor autor) {
        Cursor cursor = mContext.getContentResolver().query(
                MensageriaContract.Autores.CONTENT_URI,
                new String[]{MensageriaContract.Autores._ID},
                MensageriaContract.Autores.COLUNA_EMAIL + " = ?",
                new String[]{autor.getEmail()},
                null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return autor.getId();
        } else {
            ContentValues dadosAutor = new ContentValues();
            dadosAutor.put(MensageriaContract.Autores._ID, autor.getId());
            dadosAutor.put(MensageriaContract.Autores.COLUNA_NOME, autor.getNome());
            dadosAutor.put(MensageriaContract.Autores.COLUNA_EMAIL, autor.getEmail());
            dadosAutor.put(MensageriaContract.Autores.COLUNA_EMAIL_CONFIRMADO, autor.isEmailConfirmado() ? 1 : 0);
            dadosAutor.put(MensageriaContract.Autores.COLUNA_ULTIMO_ACESSO, autor.getUltimoAcesso());

            Uri uriInsercao = mContext.getContentResolver().insert(
                    MensageriaContract.Autores.CONTENT_URI,
                    dadosAutor
            );
            cursor.close();
            return parseId(uriInsercao);
        }
    }


    public long addConversa(Conversa conversa) {
        Cursor cursor = mContext.getContentResolver().query(
                MensageriaContract.Conversas.CONTENT_URI,
                new String[]{MensageriaContract.Conversas._ID},
                MensageriaContract.Conversas._ID + " = ?",
                new String[]{String.valueOf(conversa.getId())},
                null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return conversa.getId();
        } else {
            ContentValues dadosConversa = new ContentValues();
            dadosConversa.put(MensageriaContract.Conversas._ID, conversa.getId());
            dadosConversa.put(MensageriaContract.Conversas.COLUNA_TITULO, conversa.getNome());
            dadosConversa.put(MensageriaContract.Conversas.COLUNA_DATA_CRIACAO, conversa.getDataCriacao());
            dadosConversa.put(MensageriaContract.Conversas.COLUNA_INTERATIVA, conversa.isInterativa() ? 1 : 0);

            Uri uriInsercao = mContext.getContentResolver().insert(
                    MensageriaContract.Conversas.CONTENT_URI,
                    dadosConversa
            );
            cursor.close();
            return ContentUris.parseId(uriInsercao);
        }
    }

    public int addListaMensagem(Mensagem[] arrayMensagens) {
        Vector<ContentValues> listaMensagens = new Vector<>();

        for (Mensagem mensagem : arrayMensagens) {
            addAutor(mensagem.getAutor());
            addConversa(mensagem.getChat());

            ContentValues dadosMensagem = new ContentValues();

            dadosMensagem.put(MensageriaContract.Mensagens._ID, mensagem.getId());
            dadosMensagem.put(MensageriaContract.Mensagens.COLUNA_CONTEUDO, mensagem.getConteudo());
            dadosMensagem.put(MensageriaContract.Mensagens.COLUNA_DATA_ENVIO, mensagem.getDataEnvio());
            dadosMensagem.put(MensageriaContract.Mensagens.COLUNA_FK_AUTOR, mensagem.getAutor().getId());
            dadosMensagem.put(MensageriaContract.Mensagens.COLUNA_FK_CONVERSA, mensagem.getChat().getId());
            listaMensagens.add(dadosMensagem);
        }

        if (listaMensagens.size() > 0) {
            ContentValues[] cvArray = new ContentValues[listaMensagens.size()];
            listaMensagens.toArray(cvArray);
            int qtdMensagens = mContext.getContentResolver().bulkInsert(MensageriaContract.Mensagens.CONTENT_URI,
                    cvArray);
            return qtdMensagens;
        } else {
            return 0;
        }
    }

    public Autor findAutor(long id) {
        Cursor cursor = mContext.getContentResolver().query(MensageriaContract.Autores.CONTENT_URI,
                null,
                MensageriaContract.Autores._ID + " = ?",
                new String[]{String.valueOf(id)},
                null);
        int indexEmail = cursor.getColumnIndex(MensageriaContract.Autores.COLUNA_EMAIL);
        int indexNome = cursor.getColumnIndex(MensageriaContract.Autores.COLUNA_NOME);
        int indexEmailConfirmado = cursor.getColumnIndex(MensageriaContract.Autores.COLUNA_EMAIL_CONFIRMADO);
        int indexUltimoAcesso = cursor.getColumnIndex(MensageriaContract.Autores.COLUNA_ULTIMO_ACESSO);

        Autor autor = new Autor(id,
                cursor.getString(indexEmail),
                cursor.getString(indexNome),
                cursor.getInt(indexEmailConfirmado) == 1,
                cursor.getInt(indexUltimoAcesso));
        cursor.close();
        return autor;
    }

    public Conversa findConversa(long id) {
        Cursor cursor = mContext.getContentResolver().query(MensageriaContract.Conversas.CONTENT_URI,
                null,
                MensageriaContract.Conversas._ID + " = ?",
                new String[]{String.valueOf(id)},
                null);

        int indexInterativa = cursor.getColumnIndex(MensageriaContract.Conversas.COLUNA_INTERATIVA);
        int indexNome = cursor.getColumnIndex(MensageriaContract.Conversas.COLUNA_TITULO);
        int indexDataCriacao = cursor.getColumnIndex(MensageriaContract.Conversas.COLUNA_DATA_CRIACAO);

        Conversa conversa = new Conversa(id,
                cursor.getInt(indexInterativa) == 1,
                cursor.getString(indexNome),
                cursor.getInt(indexDataCriacao));
        cursor.close();
        return conversa;

    }
}
