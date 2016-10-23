package com.tcc.mensageria.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Classe com os nomes das tabelas do banco de dados
 */
public class MensageriaContract {

    public static final String CONTENT_AUTHORITY = "com.tcc.mensageria";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MENSAGEM = "mensagens";
    public static final String PATH_REMETENTE = "remetentes";

    /**
     * Tabela de mensagens
     */
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

        public static final String COLUNA_FK_REMETENTE = "fk_remetente";

        public static final String COLUNA_FAVORITO = "favorito";

        /**
         * Constroi a uri para acessar uma mensagem atraves do content provider
         * @param id id da mensagem desejada
         * @return uri para acessar a mensagem no banco
         */
        public static Uri buildMensagemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Constroi a uri do join entre a tabela mensagem com a tabela remetente
         * @return uri para acessar mensagens com dados dos repectivos remetentes
         */
        public static Uri buildMensagemComRemetente() {
            return CONTENT_URI.buildUpon().appendPath(PATH_REMETENTE).build();
        }
    }

    /**
     * Tabela de Remetentes
     */
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

        /**
         * Constroi a uri para acessar um remetente atraves do content provider
         * @param id id do remetente desejado
         * @return uri para acessar o remetente no banco
         */
        public static Uri buildRemetenteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
