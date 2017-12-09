package com.tcc.mensageria.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tcc.mensageria.model.Mensagem
import com.tcc.mensageria.model.MensagemDTO

@Dao
interface MensagemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(vararg dados: Mensagem): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(dados: List<Mensagem>): LongArray

    @Query("SELECT * FROM mensagem")
    fun listarTodos(): LiveData<List<Mensagem>>

    @Query("SELECT * FROM mensagem where id=:arg0")
    fun buscarPorId(id: Long): LiveData<Mensagem>

    @Query("SELECT m.conteudo, m.dataenvio,m.estado, a.nome nomeAutor, a.prioridade,a.id idAutor " +
            "FROM mensagem m ,autor a " +
            "on m.fk_autor = a.id and fk_conversa=:arg0")
    fun buscarPorIdConversa(id: Long): LiveData<List<MensagemDTO>>
}