package com.tcc.mensageria.service

import android.arch.persistence.room.Room
import android.content.Context
import com.tcc.mensageria.database.AppDatabase
import com.tcc.mensageria.database.AutorDao
import com.tcc.mensageria.database.ConversaDao
import com.tcc.mensageria.database.MensagemDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {

    @Singleton
    @Provides fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "mensageria").build()
    }

    @Provides fun provideAutorDao(appDatabase: AppDatabase): AutorDao {
        return appDatabase.getAutorDao()
    }

    @Provides fun provideConversaDao(appDatabase: AppDatabase): ConversaDao {
        return appDatabase.getConversaDao()
    }

    @Provides fun provideMensagemDao(appDatabase: AppDatabase): MensagemDao {
        return appDatabase.getMensagemDao()
    }
}