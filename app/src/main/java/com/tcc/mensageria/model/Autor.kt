package com.tcc.mensageria.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Autor(
        @PrimaryKey
        var id: Long? = null,
        var email: String? = null,
        var nome: String? = null,
        @ColumnInfo(name = "ultimo_acesso")
        var ultimoAcesso: Date? = null
)
