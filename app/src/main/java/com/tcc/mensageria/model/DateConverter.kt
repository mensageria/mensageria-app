package com.tcc.mensageria.model

import android.arch.persistence.room.TypeConverter
import java.util.*


class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return (if (date == null) null else date.getTime())?.toLong()
    }
}