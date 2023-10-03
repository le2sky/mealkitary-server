package com.mealkitary.shop.domain.shop.address

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Coordinates(
    @Column(name = "longitude", nullable = false)
    val longitude: Double,
    @Column(name = "latitude", nullable = false)
    val latitude: Double
) {

    companion object {
        fun of(longitude: Double, latitude: Double): Coordinates {
            checkIsCoordinateRange(longitude, latitude)

            return Coordinates(longitude, latitude)
        }

        private fun checkIsCoordinateRange(longitude: Double, latitude: Double) {
            if (longitude !in -180.0..180.0 || latitude !in -90.0..90.0) {
                throw IllegalArgumentException("유효하지 않은 좌표 범위입니다.")
            }
        }
    }
}
