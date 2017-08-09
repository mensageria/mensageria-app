package com.tcc.mensageria

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tcc.mensageria.model.database.AppDatabase
import com.tcc.mensageria.model.database.AutorDao
import com.tcc.mensageria.model.database.ConversaDao
import com.tcc.mensageria.model.database.MensagemDao
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class) class DbTests {
    private lateinit var mAutorDao: AutorDao
    private lateinit var mConversaDao: ConversaDao
    private lateinit var mMensagemDao: MensagemDao
    private lateinit var mDb: AppDatabase

    @Before fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        mAutorDao = mDb.getAutorDao()
        mConversaDao = mDb.getConversaDao()
        mMensagemDao = mDb.getMensagemDao()
    }

    @After @Throws(IOException::class) fun closeDb() {
        mDb.close()
    }

    @Test fun testLeituraEescritaAutor() {
        val valor = TestUtils.criarAutor(1)
        var valores = mAutorDao.inserir(valor)
        Assert.assertTrue("nenhum valor inserido no banco", valores.isNotEmpty())
        valores = mAutorDao.inserir(valor)
        Assert.assertTrue("Valor repetido inserido", valores.size == 1)
        var valorBanco = mAutorDao.buscarPorId(valor.id!!).blockingObserve()
        Assert.assertEquals("valor diferente no banco", valor, valorBanco)
    }

    @Test fun testLeituraEescritaConversa() {
        val valor = TestUtils.criarConversa(1)
        var valores = mConversaDao.inserir(valor)
        Assert.assertTrue("nenhum valor inserido no banco", valores.isNotEmpty())
        valores = mConversaDao.inserir(valor)
        Assert.assertTrue("Valor repetido inserido", valores.size == 1)
        var valorBanco = mConversaDao.buscarPorId(valor.id!!).blockingObserve()
        Assert.assertEquals("valor diferente no banco", valor, valorBanco)
    }

    @Test fun testLeituraEescritaMensagem() {
        mAutorDao.inserir(TestUtils.criarAutor(1))
        mConversaDao.inserir(TestUtils.criarConversa(1))

        val valor = TestUtils.criarMensagem(2)
        var valores = mMensagemDao.inserir(valor)
        Assert.assertTrue("nenhum valor inserido no banco", valores.isNotEmpty())
        valores = mMensagemDao.inserir(valor)
        Assert.assertTrue("Valor repetido inserido", valores.size == 1)
        var valorBanco = mMensagemDao.buscarPorId(valor.id!!).blockingObserve()
        Assert.assertEquals("valor diferente no banco", valor, valorBanco)
    }

    fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val innerObserver = Observer<T> {
            value = it
            latch.countDown()
        }
        observeForever(innerObserver)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}

