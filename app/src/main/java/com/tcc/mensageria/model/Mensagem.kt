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
        var estado: Char? = 'R',
        @ColumnInfo(name = "fk_autor")
        var autorId: Long? = null,
        @ColumnInfo(name = "fk_conversa")
        var conversaId: Long? = null
) {

}
