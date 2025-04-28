package com.job.core.resume.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.job.core.resume.domain.ExperienceItem
import com.job.core.resume.domain.Resume
import com.job.core.resume.domain.command.CreateResumeDomainCommand
import com.job.core.resume.domain.command.UpdateResumeDomainCommand
import com.job.generated.jooq.tables.Resume.RESUME
import com.job.library.jackson.fromJsonbOrNull
import com.job.library.jackson.listFromJsonbOrNull
import com.job.library.jackson.wrapAsJsonbOrNull
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.jooq.getOrNull
import com.job.library.jooq.mapper.RecordMapper
import org.jooq.impl.DSL
import java.util.UUID


class ResumeDao(
    private val objectMapper: ObjectMapper,
    private val jooqR2dbcContextFactory: JooqR2dbcContextFactory,
) {

    private val resumeMapper: RecordMapper<Resume> = {
        Resume(
            id = it[RESUME.ID],
            userId = it[RESUME.USER_ID],
            title = it[RESUME.TITLE],
            summary = it.getOrNull(RESUME.SUMMARY),
            createdAtMillis = it[RESUME.CREATED_AT_MILLIS],
            editedAtMillis = it[RESUME.EDITED_AT_MILLIS],
            isActive = it[RESUME.IS_ACTIVE],
            experience = objectMapper.listFromJsonbOrNull(
                value = it.getOrNull(RESUME.EXPERIENCE),
                elementType = ExperienceItem::class,
            ),
        )
    }

    fun create(cmd: CreateResumeDomainCommand) {
        jooqR2dbcContextFactory.use {
            insertInto(RESUME)
                .set(RESUME.ID, cmd.id)
                .set(RESUME.USER_ID, cmd.userId)
                .set(RESUME.CREATED_AT_MILLIS, cmd.createdAtMillis)
                .set(RESUME.EDITED_AT_MILLIS, cmd.createdAtMillis)
                .set(RESUME.SUMMARY, cmd.summary)
                .set(RESUME.TITLE, cmd.title)
                .set(RESUME.IS_ACTIVE, cmd.isActive)
                .set(RESUME.EXPERIENCE, objectMapper.wrapAsJsonbOrNull(cmd.experience))
        }
    }

    fun update(cmd: UpdateResumeDomainCommand) {
        jooqR2dbcContextFactory.use {
            update(RESUME)
                .set(RESUME.ID, cmd.id)
                .set(RESUME.EDITED_AT_MILLIS, cmd.editedAtMillis)
                .set(RESUME.SUMMARY, cmd.summary)
                .set(RESUME.TITLE, cmd.title)
                .set(RESUME.IS_ACTIVE, cmd.isActive)
                .set(RESUME.EXPERIENCE, objectMapper.wrapAsJsonbOrNull(cmd.experience))
                .where(
                    RESUME.ID.eq(cmd.id),
                    RESUME.USER_ID.eq(cmd.userId),
                )
        }
    }

    fun getAllByUserId(userId: UUID): List<Resume> {
        return jooqR2dbcContextFactory.fetchManyAndAwait(
            mapper = resumeMapper
        ) {
            select(DSL.asterisk())
                .from(RESUME)
                .where(RESUME.USER_ID.eq(userId))
        }
    }

    fun findById(resumeId: UUID): Resume? {
        return jooqR2dbcContextFactory.fetchOneAndAwaitNullable(
            mapper = resumeMapper,
        ) {
            select(DSL.asterisk())
                .from(RESUME)
                .where(RESUME.ID.eq(resumeId))
        }
    }
}