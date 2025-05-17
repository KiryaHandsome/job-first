package com.job.core.company.exception

import com.job.core.vacancy.dto.exception.VacancyErrorCode
import com.job.library.common.exception.BaseException
import java.util.UUID

class UserAlreadyRegisteredInCompanyException(
    userId: UUID,
) : BaseException() {
    override val code: String = "${VacancyErrorCode.BASE_CODE}.user_already_in_company"

    override val message: String = "User $userId already registered in company"
}