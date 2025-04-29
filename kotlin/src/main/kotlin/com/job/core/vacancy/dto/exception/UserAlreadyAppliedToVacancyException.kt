package com.job.core.vacancy.dto.exception

import com.job.library.common.exception.BaseException
import java.util.UUID

class UserAlreadyAppliedToVacancyException(
    userId: UUID,
    vacancyId: UUID,
) : BaseException() {
    override val code: String = "${VacancyErrorCode.BASE_CODE}.already_applied"

    override val message: String = "User $userId already applied to vacancy $vacancyId"
}