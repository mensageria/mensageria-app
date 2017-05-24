package com.tcc.mensageria.model

data class Autor(val id: Long?,
                 var email: String,
                 var nome: String,
                 var isEmailConfirmado: Boolean,
                 var ultimoAcesso: Long)
