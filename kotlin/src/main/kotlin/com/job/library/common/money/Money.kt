package com.job.library.common.money

import java.math.BigInteger

data class Money(
    val amount: BigInteger,
    val currency: Currency,
)