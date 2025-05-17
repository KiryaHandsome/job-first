package com.job.core.vacancy.handler

import com.job.core.email.model.EmailInfo
import com.job.core.email.service.EmailSender
import com.job.core.vacancy.command.ReviewVacancyApplyCommand
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.ApplyStatus
import com.job.core.vacancy.dto.ReviewVacancyApplyAction
import com.job.library.command.UnitHandler
import java.util.UUID

class ReviewVacancyApplyCommandHandler(
    private val vacancyDao: VacancyDao,
    private val emailSender: EmailSender,
) : UnitHandler<ReviewVacancyApplyCommand> {

    override suspend fun handle(command: ReviewVacancyApplyCommand) {
        val applyStatus = when (command.action) {
            ReviewVacancyApplyAction.REJECT -> ApplyStatus.DECLINED
            ReviewVacancyApplyAction.ACCEPT -> ApplyStatus.INVITED
        }

        vacancyDao.editApplyStatus(
            id = command.id,
            status = applyStatus
        )
    }

    private fun sendEmail(applyStatus: ApplyStatus, applyId: UUID) {
        emailSender.sendEmail(
            email = EmailInfo(
                to = "",
                subject = "Результат отклика на вакансию",
                body = TODO(),
            )
        )
    }
}