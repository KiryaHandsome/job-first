package com.job.core.email.service

import com.job.core.email.model.EmailInfo

interface EmailSender {

    fun sendEmail(email: EmailInfo)
}

class EmailSenderImpl(

) : EmailSender {

    override fun sendEmail(email: EmailInfo) {
        TODO("Not yet implemented")
    }
}