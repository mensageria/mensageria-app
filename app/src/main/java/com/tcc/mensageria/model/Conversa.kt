package com.tcc.mensageria.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Conversa(
        @PrimaryKey
        var id: Long? = null,
        var interativa: Boolean? = null,
        var nome: String? = null
) {
}
