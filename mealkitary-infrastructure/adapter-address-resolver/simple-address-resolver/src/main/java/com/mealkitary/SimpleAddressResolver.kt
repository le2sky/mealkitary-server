package com.mealkitary

import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import com.mealkitary.shop.domain.shop.address.ShopAddress
import com.mealkitary.shop.domain.shop.factory.AddressResolver
import org.springframework.stereotype.Component

private const val ADDRESS_MIN_LENGTH = 2

@Component
class SimpleAddressResolver : AddressResolver {

    override fun resolve(fullAddress: String): ShopAddress {
        val value = fullAddress.split(" ")

        if (value.size < ADDRESS_MIN_LENGTH) {
            throw IllegalArgumentException("주소 형식이 올바르지 않습니다.")
        }

        val region1DepthName = value[0]
        val region2DepthName = value[1]
        val region3DepthName = value.getOrNull(2) ?: ""
        val roadName = value.getOrNull(3) ?: ""

        // TODO: 좌표 및 지역 코드를 카카오 API에서 받아올 예정
        return ShopAddress.of(
            "1234567890",
            Coordinates.of(
                127.0,
                40.0
            ),
            Address.of(
                region1DepthName,
                region2DepthName,
                region3DepthName,
                roadName
            )
        )
    }
}
