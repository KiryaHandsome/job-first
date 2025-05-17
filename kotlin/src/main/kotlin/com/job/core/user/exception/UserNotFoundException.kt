package com.job.core.user.exception

import com.job.library.common.exception.BaseException

class UserNotFoundException(
    userId: String,
) : BaseException() {
    override val code: String = "${UserErrorCode.BASE_CODE}.not_found"

    override val message: String = "User with id $userId not found"
}
