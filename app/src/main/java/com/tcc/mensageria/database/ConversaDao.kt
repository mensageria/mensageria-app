package com.tcc.mensageria.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tcc.mensageria.model.Conversa
import com.tcc.mensageria.model.ConversaDTO

@Dao
interface ConversaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(vararg dados: Conversa): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(dados: List<Conversa>): LongArray

    @Query("SELECT * FROM conversa")
    fun listarTodos(): LiveData<List<Conversa>>

    @Query("SELECT * FROM conversa where id=:arg0")
    fun buscarPorId(id: Long): LiveData<Conversa>

    @Query("SELECT c.id,c.nome titulo,a.nome autor, m.conteudo,m.estado ,MAX(dataEnvio) dataEnvio from Conversa c,Mensagem m ,Autor a on  m.fk_conversa = c.id and m.fk_autor = a.id group by c.id")
    fun buscarUltimas(): LiveData<List<ConversaDTO>>
}