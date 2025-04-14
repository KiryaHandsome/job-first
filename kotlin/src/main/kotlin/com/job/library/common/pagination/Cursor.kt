package com.job.library.common.pagination

data class Cursor(
    val pageNumber: Int,
    val pageSize: Int,
) {

    fun validate() {
        require(pageNumber >= 0) { "pageNumber must be greater than or equal to 0" }
        require(pageSize in 1..200) { "pageSize must be between 1 and 200" }
    }

    fun getOffset() = pageNumber * pageSize

    fun getLimit() = pageSize
}
