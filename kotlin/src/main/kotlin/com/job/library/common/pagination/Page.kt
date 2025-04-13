package com.job.library.common.pagination

data class Page<T>(
    val data: List<T>,
    val pageInfo: PageInfo,
)

data class PageInfo(
    val totalPages: Int,
    val pageSize: Int,
    val pageNumber: Int,
)

