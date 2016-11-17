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
    public static final String PATH_AUTOR = "autor";
    public static final String PATH_CONVERSA = "conversa";

    /**
     * Tabela de mensagens
     */
    public static final class Mensagens implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENSAGEM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENSAGEM;
        public static final String CONTENT_TYPE_COM_AUTOR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENSAGEM +
                        "/" + PATH_AUTOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENSAGEM;

        public static final String NOME_TABELA = "mensagens";

        public static final String COLUNA_CONTEUDO = "conteudo";

        public static final String COLUNA_DATA_ENVIO = "data_envio";

        public static final String COLUNA_FK_AUTOR = "fk_autor";

        public static final String COLUNA_FK_CONVERSA = "fk_conversa";


        /**
         * Constroi a uri para acessar uma mensagem atraves do content provider
         * @param id id da mensagem desejada
         * @return uri para acessar a mensagem no banco
         */
        public static Uri buildMensagemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Constroi a uri do join entre a tabela mensagem com a tabela autor
         * @return uri para acessar mensagens com dados dos repectivos autores
         */
        public static Uri buildMensagemComAutor() {
            return CONTENT_URI.buildUpon().appendPath(PATH_AUTOR).build();
        }
    }

    /**
     * Tabela de Autores
     */
    public static final class Autores implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_AUTOR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUTOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUTOR;

        public static final String NOME_TABELA = "autores";

        public static final String COLUNA_NOME = "nome";

        public static final String COLUNA_EMAIL = "email";

        //TODO colocar foto do perfil
        //public static final String COLUNA_IMAGEM = "imagem";

        /**
         * Constroi a uri para acessar um autor atraves do content provider
         * @param id id do autor desejado
         * @return uri para acessar o autor no banco
         */
        public static Uri buildAutorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Tabela de Conversas
     */
    public static final class Conversas implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONVERSA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONVERSA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONVERSA;

        public static final String NOME_TABELA = "conversas";

        public static final String COLUNA_NOME = "nome";

        public static final String COLUNA_INTERATIVA = "interativa";

        /**
         * Constroi a uri para acessar uma conversa atraves do content provider
         * @param id id da conversa desejado
         * @return uri para acessar a conversa no banco
         */
        public static Uri buildConversaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
