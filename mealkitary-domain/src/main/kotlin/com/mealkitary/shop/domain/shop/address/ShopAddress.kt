package com.mealkitary.shop.domain.shop.address

import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Embedded

private const val CITY_CODE_LENGTH = 10

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
            if (cityCode.length != CITY_CODE_LENGTH) {
                throw IllegalArgumentException("올바른 지역 코드가 아닙니다. (행정동 지역 코드는 10자리)")
            }
        }
    }
}
