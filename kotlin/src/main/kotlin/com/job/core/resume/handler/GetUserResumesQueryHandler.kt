package com.job.core.resume.handler

import com.job.core.resume.command.GetUserResumesQuery
import com.job.core.resume.dao.ResumeDao
import com.job.core.resume.domain.Resume
import com.job.library.command.CommandHandler
import com.job.library.command.SubjectRegistry

class GetUserResumesQueryHandler(
    private val resumeDao: ResumeDao,
    private val subjectRegistry: SubjectRegistry,
) : CommandHandler<GetUserResumesQuery, List<Resume>> {

    override suspend fun handle(command: GetUserResumesQuery): List<Resume> {
        val subject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

        return resumeDao.getAllByUserId(subject.userId)
    }

//    private fun Resume.toDto(): ResumeDto = ResumeDto(
//        id = id,
//        userId = userId,
//        title = title,
//        summary = summary,
//        createdAtMillis = createdAtMillis,
//        editedAtMillis = editedAtMillis,
//        isActive = isActive,
//        experience = experience,
//    )
}