package com.mealkitary.brn.payload

class OpenApiBrnStatusResponse(
    val status_code: String,
    val match_cnt: Int,
    val request_cnt: Int,
    val data: List<OpenApiBrnStatus>
)
