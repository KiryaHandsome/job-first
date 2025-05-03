package com.job.core.company.exception

import com.job.library.common.exception.BaseException

class UserNotRegisteredInCompanyException : BaseException() {

    override val code: String = "company.user_not_registered_in_company"

    override val message: String = "User not registered into Company"
}