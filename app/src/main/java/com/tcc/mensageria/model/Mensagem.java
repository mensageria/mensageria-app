package com.tcc.mensageria.model;

public class Mensagem {

    private Long id;
    private String conteudo;

    private long dataEnvio;

    private Autor autor;

    private Conversa chat;

    public Mensagem(Long id, String conteudo, long dataEnvio, Autor autor, Conversa chat) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.autor = autor;
        this.chat = chat;
    }

    public Mensagem(String conteudo, long dataEnvio, Autor autor, Conversa conversa) {
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.autor = autor;
        this.chat = conversa;
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

    public Conversa getChat() {
        return chat;
    }

    public void setChat(Conversa chat) {
        this.chat = chat;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Mensagem{" +
                "id=" + id +
                ", conteudo='" + conteudo + '\'' +
                ", dataEnvio=" + dataEnvio +
                ", autor=" + autor.toString() +
                ", chat=" + chat.toString() +
                '}';
    }
}
