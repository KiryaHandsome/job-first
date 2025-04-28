package com.job.library.command

import com.job.library.common.security.Subject
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class SubjectRegistry {

    /**
     * Key is command id
     */
    private val subjects = ConcurrentHashMap<UUID, Subject>()

    fun findSubject(commandId: UUID): Subject? {
        return subjects[commandId]
    }

    fun getSubject(commandId: UUID): Subject {
        return findSubject(commandId = commandId) ?: error("Subject for command with id=$commandId not found")
    }

    fun registerSubject(commandId: UUID, subject: Subject) {
        subjects[commandId] = subject
    }

    fun removeSubject(commandId: UUID) {
        subjects.remove(commandId)
    }
}