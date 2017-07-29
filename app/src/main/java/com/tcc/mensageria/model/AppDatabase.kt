package com.tcc.mensageria.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.tcc.mensageria.model.dao.AutorDao
import com.tcc.mensageria.model.dao.ConversaDao
import com.tcc.mensageria.model.dao.MensagemDao


@Database(entities = arrayOf(Autor::class, Mensagem::class, Conversa::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAutorDao(): AutorDao
    abstract fun getMensagemDao(): MensagemDao
    abstract fun getConversaDao(): ConversaDao
}