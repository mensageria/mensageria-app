package com.tcc.mensageria.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.tcc.mensageria.model.Autor
import com.tcc.mensageria.model.Conversa
import com.tcc.mensageria.model.Mensagem


@Database(entities = arrayOf(Autor::class, Mensagem::class, Conversa::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAutorDao(): AutorDao
    abstract fun getMensagemDao(): MensagemDao
    abstract fun getConversaDao(): ConversaDao
}