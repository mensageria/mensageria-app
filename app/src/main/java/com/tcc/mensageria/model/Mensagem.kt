package com.tcc.mensageria.model

import android.arch.persistence.room.*
import java.util.*

@Entity(foreignKeys = arrayOf(
        ForeignKey(entity = Autor::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("fk_autor")),
        ForeignKey(entity = Conversa::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("fk_conversa"))
), indices = arrayOf(Index("fk_autor"), Index("fk_conversa")))
data class Mensagem(
        @PrimaryKey
        var id: Long? = null,
        var conteudo: String? = null,
        var dataEnvio: Date? = null,
        var recebida: Boolean? = null,
        var enviada: Boolean? = null,
        @ColumnInfo(name = "fk_autor")
        var autorId: Int? = null,
        @ColumnInfo(name = "fk_conversa")
        var conversaId: Int? = null
) {


}
