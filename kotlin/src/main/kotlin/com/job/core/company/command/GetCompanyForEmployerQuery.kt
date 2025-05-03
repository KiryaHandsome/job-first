package com.job.core.company.command

import com.job.core.company.domain.Company
import com.job.core.user.domain.UserRole
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet

class GetCompanyForEmployerQuery : AbstractCommand<Company>() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.company.get_company_for_employer"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.EMPLOYER)
    }
}
