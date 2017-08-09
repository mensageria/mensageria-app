package com.tcc.mensageria.model.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tcc.mensageria.model.Conversa

@Dao
interface ConversaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public fun inserir(vararg dados: Conversa): LongArray

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserir(dados: List<Conversa>): LongArray

    @Query("SELECT * FROM conversa")
    fun listarTodos(): LiveData<List<Conversa>>

    @Query("SELECT * FROM conversa where id=:arg0")
    fun buscarPorId(id: Long): LiveData<Conversa>
}