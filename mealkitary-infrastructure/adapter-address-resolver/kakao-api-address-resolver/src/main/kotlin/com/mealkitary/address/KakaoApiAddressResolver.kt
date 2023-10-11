package com.mealkitary.address

import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import com.mealkitary.shop.domain.shop.ShopAddress
import com.mealkitary.shop.domain.shop.factory.AddressResolver
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
@Component
class KakaoApiAddressResolver(
    private val kakaoApiWebClient: KakaoApiWebClient
) : AddressResolver {

    override fun resolve(fullAddress: String): ShopAddress {
        val kakaoApiAddressResponse = kakaoApiWebClient.requestAddress(fullAddress)

        val (x, y, address, roadAddress) = kakaoApiAddressResponse.document

        val (longitude, latitude) = listOf(x, y).map {
            it.toDoubleOrNull() ?: throw IllegalArgumentException("유효하지 않은 좌표 범위입니다.")
        }

        return ShopAddress.of(
            roadAddress.h_code,
            Coordinates.of(
                longitude,
                latitude
            ),
            Address.of(
                address.region_1depth_name,
                address.region_2depth_name,
                address.region_3depth_name,
                roadAddress.road_name
            )
        )
    }
}
