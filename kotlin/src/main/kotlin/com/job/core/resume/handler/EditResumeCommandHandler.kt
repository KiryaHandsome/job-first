package com.job.core.resume.handler

import com.job.core.resume.command.EditResumeCommand
import com.job.core.resume.dao.ResumeDao
import com.job.core.resume.domain.command.UpdateResumeDomainCommand
import com.job.library.command.SubjectRegistry
import com.job.library.command.UnitHandler
import java.time.Instant

class EditResumeCommandHandler(
    private val resumeDao: ResumeDao,
    private val subjectRegistry: SubjectRegistry,
) : UnitHandler<EditResumeCommand> {

    override suspend fun handle(command: EditResumeCommand) {
        val subject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

        resumeDao.update(
            cmd = UpdateResumeDomainCommand(
                id = command.id,
                userId = subject.userId,
                title = command.title,
                summary = command.summary,
                editedAtMillis = Instant.now().toEpochMilli(),
                isActive = command.isActive,
                experience = command.experience,
            )
        )
    }
}