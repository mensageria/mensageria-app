package com.tcc.mensageria.model;

import android.provider.BaseColumns;


public class MensageriaContract {

    public static final class Mensagens implements BaseColumns {

        public static final String NOME_TABELA = "mensagens";

        public static final String COLUNA_TITULO = "titulo";

        public static final String COLUNA_CONTEUDO = "conteudo";

        public static final String COLUNA_FK_REMETENTE = "remetente";

        public static final String COLUNA_FAVORITO = "favorito";
    }

    public static final class Remetentes implements BaseColumns{

        public static final String NOME_TABELA = "remetentes";

        public static final String COLUNA_NOME = "nome";

        public static final String COLUNA_EMAIL = "email";

        //TODO colocar foto do perfil
        //public static final String COLUNA_IMAGEM = "imagem";
    }
}
