package com.tcc.mensageria.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tcc.mensageria.model.Autor

@Dao
interface AutorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(vararg dados: Autor): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(dados: List<Autor>): LongArray

    @Query("SELECT * FROM autor")
    fun listarTodos(): LiveData<List<Autor>>

    @Query("SELECT * FROM autor where id= :arg0")
    fun buscarPorId(id: Long): LiveData<Autor>
}