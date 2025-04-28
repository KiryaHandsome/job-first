package com.job.library.command

import com.job.library.common.uuid.Uuid
import java.util.UUID

interface Command<Response> {

    val uniqueCommandId: UUID
}

abstract class AbstractCommand<Response> : Command<Response> {
    override val uniqueCommandId: UUID = Uuid.v7()
}

typealias UnitCommand = AbstractCommand<Unit>