package com.tcc.mensageria.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tcc.mensageria.model.Mensagem

@Dao
interface MensagemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public fun inserir(vararg dados: Mensagem): LongArray

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserir(dados: List<Mensagem>): LongArray

    @Query("SELECT * FROM mensagem")
    fun listarTodos(): LiveData<List<Mensagem>>

    @Query("SELECT * FROM mensagem where id=:arg0")
    fun buscarPorId(id: Long): LiveData<Mensagem>

    @Query("SELECT * FROM mensagem  where fk_conversa=:arg0")
    fun buscarPorIdConversa(id: Long): LiveData<List<Mensagem>>
}