package com.job.library.command

interface CommandHandler<Cmd : Command<Response>, Response> {

    suspend fun handle(command: Cmd): Response
}

typealias UnitHandler<Cmd> = CommandHandler<Cmd, Unit>