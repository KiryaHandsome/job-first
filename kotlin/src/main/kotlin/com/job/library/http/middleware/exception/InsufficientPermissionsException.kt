package com.job.library.http.middleware.exception

import com.job.library.common.exception.BaseException

class InsufficientPermissionsException(
    commandUri: String,
) : BaseException() {

    override val code: String = "security.insufficient_permissions"

    override val message: String = "Insufficient rights to perform the operation: '$commandUri'"
}