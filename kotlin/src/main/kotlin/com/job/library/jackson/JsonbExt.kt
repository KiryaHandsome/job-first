package com.job.library.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.JSONB
import kotlin.reflect.KClass


fun ObjectMapper.wrapAsJsonbOrNull(value: Any?): JSONB? {
    return JSONB.jsonbOrNull(this.writeValueAsString(value))
}

fun <E : Any> ObjectMapper.listFromJsonbOrNull(value: JSONB?, elementType: KClass<E>): List<E>? {
    val data = value?.data() ?: return null

    val result: List<E> = this.readValue(
        data,
        this.typeFactory.constructCollectionType(List::class.java, elementType.java)
    )

    return result
}

fun <T> ObjectMapper.fromJsonbOrNull(value: JSONB?): T? {
    val data = value?.data() ?: return null

    return this.readValue(data, object : TypeReference<T>() {})
}

//private fun String.isArray() = this.startsWith("[")