package com.job.core.resume.handler

import com.job.core.resume.command.GetResumeByIdQuery
import com.job.core.resume.dao.ResumeDao
import com.job.core.resume.domain.Resume
import com.job.library.command.CommandHandler

class GetResumeByIdQueryHandler(
    private val resumeDao: ResumeDao,
) : CommandHandler<GetResumeByIdQuery, Resume> {

    override suspend fun handle(command: GetResumeByIdQuery): Resume {
        return resumeDao.findById(command.resumeId) ?: error("Resume with id=$${command.resumeId} not found")
    }
}