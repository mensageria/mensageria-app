package com.tcc.mensageria.service

import com.tcc.mensageria.database.AutorDao
import com.tcc.mensageria.database.ConversaDao
import com.tcc.mensageria.database.MensagemDao
import javax.inject.Inject


class DBService @Inject constructor(val autorDao: AutorDao,
                                    val conversaDao: ConversaDao,
                                    val mensagemDao: MensagemDao) {


}
