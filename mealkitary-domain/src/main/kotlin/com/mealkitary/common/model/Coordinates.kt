package com.mealkitary.common.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Coordinates(
    @Column(name = "longitude", nullable = false)
    val longitude: String,
    @Column(name = "latitude", nullable = false)
    val latitude: String
) {

    companion object {
        fun of(longitude: String, latitude: String): Coordinates {
            checkIsCoordinateRange(longitude, latitude)

            return Coordinates(longitude, latitude)
        }

        private fun checkIsCoordinateRange(longitude: String, latitude: String) {
            if (longitude.toDouble() !in -180.0..180.0 || latitude.toDouble() !in -90.0..90.0) {
                throw IllegalArgumentException("유효하지 않은 좌표 범위입니다.")
            }
        }
    }
}
