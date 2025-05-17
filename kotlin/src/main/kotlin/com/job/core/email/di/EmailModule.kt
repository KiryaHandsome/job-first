package com.job.core.email.di

import com.job.core.email.service.EmailSender
import com.job.core.email.service.EmailSenderImpl
import com.job.library.di.autoBindWith
import org.kodein.di.DI

val emailModule = DI.Module("emailModule") {

    autoBindWith<EmailSender, EmailSenderImpl>()
}