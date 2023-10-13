package com.mealkitary.address

import com.mealkitary.address.payload.Document
import com.mealkitary.address.payload.Meta
import com.mealkitary.address.payload.RoadAddress
import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import com.mealkitary.shop.domain.shop.ShopAddress
import com.mealkitary.shop.domain.shop.factory.AddressResolver
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

private const val INVALID_ROAD_ADDR_MESSAGE = "올바른 도로명 주소를 입력해주세요."
private const val INVALID_COORDINATE_FORMAT = "유효하지 않은 좌표 범위입니다."
private const val BUILDING_NO_DELIMITER = "-"
private const val VALID_ADDR_TYPE = "ROAD_ADDR"

@Primary
@Component
class KakaoApiAddressResolver(
    private val kakaoApiWebClient: KakaoApiWebClient
) : AddressResolver {

    override fun resolve(fullAddress: String): ShopAddress {
        val (documents, meta) = kakaoApiWebClient.requestAddress(fullAddress, "https://dapi.kakao.com")
        checkHasDocument(meta, documents)
        val document = findFirstMatchedDocument(documents)

        return ShopAddress.of(resolveCityCode(document), resolveCoordinates(document), resolveAddress(document))
    }

    private fun checkHasDocument(meta: Meta, documents: List<Document>) {
        if (meta.total_count < 0 || documents.isEmpty()) {
            throw IllegalArgumentException(INVALID_ROAD_ADDR_MESSAGE)
        }
    }

    private fun findFirstMatchedDocument(documents: List<Document>): Document {
        val firstMatchedDocument = documents[0]
        checkIsValidAddressType(firstMatchedDocument)

        return firstMatchedDocument
    }

    private fun checkIsValidAddressType(addressDocument: Document) {
        if (addressDocument.address_type != VALID_ADDR_TYPE) {
            throw IllegalArgumentException(INVALID_ROAD_ADDR_MESSAGE)
        }
    }

    private fun resolveCityCode(document: Document): String {
        requireNotNull(document.address) { INVALID_ROAD_ADDR_MESSAGE }

        return document.address.h_code
    }

    private fun resolveCoordinates(loadAddressDocument: Document): Coordinates {
        val (longitude, latitude) = listOf(loadAddressDocument.x, loadAddressDocument.y).map {
            it.toDoubleOrNull() ?: throw IllegalArgumentException(INVALID_COORDINATE_FORMAT)
        }

        return Coordinates.of(longitude, latitude)
    }

    private fun resolveAddress(document: Document): Address {
        val roadAddress: RoadAddress = requireNotNull(document.road_address) { INVALID_ROAD_ADDR_MESSAGE }
        val roadName =
            "${roadAddress.road_name} ${roadAddress.main_building_no}-${roadAddress.sub_building_no}".trim()

        return Address.of(
            roadAddress.region_1depth_name,
            roadAddress.region_2depth_name,
            roadAddress.region_3depth_name,
            if (roadName.endsWith(BUILDING_NO_DELIMITER)) {
                roadName.replace(BUILDING_NO_DELIMITER, "")
            } else roadName
        )
    }
}
