package com.job.library.common.money

enum class Currency {
    BYN,
    USD,
    RUB,
}

val currencyToMinorUnit = mapOf(
    Currency.BYN to 2,
    Currency.USD to 2,
)