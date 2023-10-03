package com.mealkitary.shop.domain.shop.address

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
class ShopAddress private constructor(
    @Column(name = "city_code", nullable = false)
    val cityCode: String,
    @Embedded
    val coordinates: Coordinates,
    @Embedded
    val address: Address
) {

    companion object {
        fun of(cityCode: String, coordinates: Coordinates, address: Address): ShopAddress {
            checkIsCityCodeLength(cityCode)

            return ShopAddress(cityCode, coordinates, address)
        }

        private fun checkIsCityCodeLength(cityCode: String) {
            if (cityCode.length != 10) {
                throw IllegalArgumentException("올바른 지역 코드가 아닙니다. (행정동 지역 코드는 10자리)")
            }
        }
    }
}
