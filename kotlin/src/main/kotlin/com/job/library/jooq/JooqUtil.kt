package com.job.library.jooq

import org.jooq.Field
import org.jooq.TableField


fun <T : Enum<T>> org.jooq.Record.findEnumValue(field: TableField<*, *>, clazz: Class<T>): T? {
    val value = this.get(field, String::class.java) ?: return null

    return java.lang.Enum.valueOf(clazz, value)
}

fun <T> org.jooq.Record.getOrNull(field: Field<T>): T? = this.get(field)
