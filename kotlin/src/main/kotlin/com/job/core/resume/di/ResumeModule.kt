package com.job.core.resume.di

import com.job.core.resume.dao.ResumeDao
import com.job.core.resume.handler.CreateResumeCommandHandler
import com.job.core.resume.handler.GetResumeByIdQueryHandler
import com.job.core.resume.handler.GetUserResumesQueryHandler
import com.job.core.resume.handler.EditResumeCommandHandler
import com.job.library.di.autoBind
import org.kodein.di.DI


val resumeModule = DI.Module("resumeModule") {
    // DAO
    autoBind<ResumeDao>()

    // handlers
    autoBind<CreateResumeCommandHandler>()
    autoBind<GetUserResumesQueryHandler>()
    autoBind<EditResumeCommandHandler>()
    autoBind<GetResumeByIdQueryHandler>()
}

