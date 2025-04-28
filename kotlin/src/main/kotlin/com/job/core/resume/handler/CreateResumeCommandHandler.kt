package com.job.core.resume.handler

import com.job.core.resume.command.CreateResumeCommand
import com.job.core.resume.dao.ResumeDao
import com.job.core.resume.domain.command.CreateResumeDomainCommand
import com.job.core.user.domain.UserRole
import com.job.library.command.SubjectRegistry
import com.job.library.command.UnitHandler
import com.job.library.common.uuid.Uuid
import java.time.Instant

class CreateResumeCommandHandler(
    private val resumeDao: ResumeDao,
    private val subjectRegistry: SubjectRegistry,
) : UnitHandler<CreateResumeCommand> {

    override suspend fun handle(command: CreateResumeCommand) {
        val subject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

        if (subject.role != UserRole.USER) {
            error("Subject ${command.uniqueCommandId} is not user")
        }

        val createdAtMillis = Instant.now().toEpochMilli()

        resumeDao.create(
            cmd = CreateResumeDomainCommand(
                id = Uuid.v7(),
                userId = subject.userId,
                title = command.title,
                summary = command.summary,
                createdAtMillis = createdAtMillis,
                isActive = command.isActive,
                experience = command.experience,
            )
        )
    }
}