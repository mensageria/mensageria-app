package com.tcc.mensageria.model;

public class Mensagem {

    private Long id;
    private String conteudo;

    private long dataEnvio;

    private Autor autor;

    private Conversa conversa;

    public Mensagem(Long id, String conteudo, long dataEnvio, Autor autor, Conversa chat) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.autor = autor;
        this.conversa = chat;
    }

    public Mensagem(String conteudo, long dataEnvio, Autor autor, Conversa conversa) {
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.autor = autor;
        this.conversa = conversa;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(long dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }


    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Conversa getConversa() {
        return conversa;
    }

    public void setConversa(Conversa conversa) {
        this.conversa = conversa;
    }

    public Long getId() {
        return id;
    }


}
