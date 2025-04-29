package com.job.library.jooq

import com.job.library.common.db.DatabaseCredentials
import com.job.library.common.pagination.Cursor
import com.job.library.common.pagination.Page
import com.job.library.common.pagination.PageInfo
import com.job.library.jooq.exception.IntegrityViolationException
import com.job.library.jooq.mapper.RecordMapper
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.R2dbcDataIntegrityViolationException
import org.jooq.Condition
import org.jooq.DMLQuery
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Select
import org.jooq.Table
import org.jooq.exception.IntegrityConstraintViolationException
import org.jooq.impl.DSL
import org.jooq.impl.DSL.count
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class JooqR2dbcContextFactory(
    private val databaseCredentials: DatabaseCredentials,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(JooqR2dbcContextFactory::class.java)
    }

    private val connectionFactory: ConnectionFactory = buildConnectionFactory()

    private fun buildConnectionFactory() = with(databaseCredentials) {
        ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, driver)
                .option(ConnectionFactoryOptions.HOST, host)
                .option(ConnectionFactoryOptions.PORT, port)
                .option(ConnectionFactoryOptions.USER, userName)
                .option(ConnectionFactoryOptions.PASSWORD, password)
                .option(ConnectionFactoryOptions.DATABASE, databaseName)
                .build()
        )
    }

    fun <T> fetchOneAndAwaitNullable(
        mapper: RecordMapper<T>,
        block: DSLContext.() -> Select<out org.jooq.Record>
    ): T? {
        val dslContext = DSL.using(connectionFactory)

        val select = block(dslContext)

        val value = Flux.from(select)
            .map { mapper(it) }

        return value.blockFirst()
    }

    fun <T> fetchManyAndAwait(
        mapper: RecordMapper<T>,
        block: DSLContext.() -> Select<*>
    ): List<T> {
        val dslContext = DSL.using(connectionFactory)

        val select = block(dslContext)

        val value = Flux.from(select)
            .map { mapper(it) }
            .collectList()

        return value.block()
    }

    fun <T> fetchPageAndAwait(
        cursor: Cursor,
        mapper: RecordMapper<T>,
        table: Table<*>,
        fields: Array<Field<*>>,
        whereConditions: List<Condition> = emptyList(),
    ): Page<T> {
        val dslContext = DSL.using(connectionFactory)

        val select = dslContext
            .select(*fields)
            .from(table)
            .where(whereConditions)
            .offset(cursor.getOffset())
            .limit(cursor.getLimit())

        val data = Flux.from(select)
            .map { mapper(it) }
            .collectList()

        val totalCountRecord =
            Mono.from(
                dslContext
                    .select(count().`as`("total_count"))
                    .from(table)
                    .where(whereConditions)
            ).block()
        val totalCount = totalCountRecord?.get("total_count", Int::class.java) ?: 0

        return Page(
            data = data.block(),
            pageInfo = PageInfo(
                totalPages = computeTotalPages(totalElements = totalCount, pageSize = cursor.pageSize),
                pageSize = cursor.pageSize,
                pageNumber = cursor.pageNumber,
            )
        )
    }

    private fun computeTotalPages(totalElements: Int, pageSize: Int): Int {
        return (totalElements + pageSize - 1) / pageSize
    }

    // TODO: add on duplicate factory
    fun use(
        duplicateFactory: (Throwable) -> Throwable = { IntegrityViolationException(it) },
        block: DSLContext.() -> DMLQuery<out org.jooq.Record>
    ) {
        try {
            val dslContext = DSL.using(connectionFactory)

            val query = block(dslContext)

            val result = Flux.from(query).blockFirst()

            if (result == null || result < 1) {
                error("No rows were updated") // todo: throw only if needed
            }
        } catch (e: IntegrityConstraintViolationException) {
            throw duplicateFactory(e)
        }

    }
}