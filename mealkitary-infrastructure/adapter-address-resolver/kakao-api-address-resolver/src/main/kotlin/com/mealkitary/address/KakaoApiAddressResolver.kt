package com.mealkitary.address

import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import com.mealkitary.shop.domain.shop.address.ShopAddress
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

        val x = kakaoApiAddressResponse.document.x
        val y = kakaoApiAddressResponse.document.y
        val roadAddress = kakaoApiAddressResponse.document.road_address
        val address = kakaoApiAddressResponse.document.address

        return ShopAddress.of(
            roadAddress.h_code,
            Coordinates.of(
                x,
                y
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
