package com.tcc.mensageria.model;

public class Autor {
    private Long id;
    private String email;
    private String nome;
    private boolean emailConfirmado;
    private long ultimoAcesso;

    public Autor() {
    }

    public Autor(Long id, String email, String nome, boolean emailConfirmado, long ultimoAcesso) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.emailConfirmado = emailConfirmado;
        this.ultimoAcesso = ultimoAcesso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmado() {
        return emailConfirmado;
    }

    public void setEmailConfirmado(boolean emailConfirmado) {
        this.emailConfirmado = emailConfirmado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(long ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Long getId() {
        return id;
    }

}
