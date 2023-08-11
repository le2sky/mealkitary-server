package com.mealkitary.common.utils

import java.util.UUID

class UUIDUtils {

    companion object {
        fun fromString(uuidString: String): UUID {
            try {
                return UUID.fromString(uuidString)
            } catch (e: Exception) {
                throw IllegalArgumentException("잘못된 UUID 형식입니다.")
            }
        }
    }
}
