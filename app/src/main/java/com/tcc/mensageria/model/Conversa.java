package com.tcc.mensageria.model;

public class Conversa {


    private Long id;

    private boolean interativa;
    private String nome;
    private long dataCriacao;

    public Conversa() {
    }

    public Conversa(Long id, boolean interativa, String titulo, int dataCriacao) {
        this.id = id;
        this.interativa = interativa;
        this.nome = titulo;
        this.dataCriacao = dataCriacao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(int dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isInterativa() {
        return interativa;
    }

    public void setInterativa(boolean interativa) {
        this.interativa = interativa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Conversa{" +
                "id=" + id +
                ", interativa=" + interativa +
                ", nome='" + nome + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
