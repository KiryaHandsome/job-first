package com.job.core.vacancy.command

import com.job.library.command.UnitCommand
import com.job.library.command.UriAware

data class CreateVacancyCommand(
    val title: String,
    val description: String,
    val company: String,
    val location: String,
    val salary: String?,
    val requirements: List<String>,
    val postedDate: String
) : UnitCommand {

    companion object : UriAware {
        override fun uri(): String = "com.job.vacancy.create"
    }
}
