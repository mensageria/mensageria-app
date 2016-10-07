package com.tcc.mensageria.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class MensageriaContract {

    public static final String CONTENT_AUTHORITY = "com.tcc.mensageria";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MENSAGEM = "mensagens";
    public static final String PATH_REMETENTE = "remetentes";

    public static final class Mensagens implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENSAGEM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENSAGEM;
        public static final String CONTENT_TYPE_COM_REMETENTE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENSAGEM +
                        "/" + PATH_REMETENTE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENSAGEM;

        public static final String NOME_TABELA = "mensagens";

        public static final String COLUNA_TITULO = "titulo";

        public static final String COLUNA_CONTEUDO = "conteudo";

        public static final String COLUNA_FK_REMETENTE = "remetente";

        public static final String COLUNA_FAVORITO = "favorito";

        public static Uri buildMensagemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMensagemComRemetente() {
            return CONTENT_URI.buildUpon().appendPath(PATH_REMETENTE).build();
        }
    }

    public static final class Remetentes implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMETENTE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REMETENTE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REMETENTE;

        public static final String NOME_TABELA = "remetentes";

        public static final String COLUNA_NOME = "nome";

        public static final String COLUNA_EMAIL = "email";

        //TODO colocar foto do perfil
        //public static final String COLUNA_IMAGEM = "imagem";

        public static Uri buildRemetenteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
