package com.tcc.mensageria.service

import com.tcc.mensageria.model.database.AutorDao
import com.tcc.mensageria.model.database.ConversaDao
import com.tcc.mensageria.model.database.MensagemDao
import javax.inject.Inject

class DBService @Inject constructor(val autorDao: AutorDao,
                                    val conversaDao: ConversaDao,
                                    val mensagemDao: MensagemDao) {
}
