package com.mealkitary.address.payload

class KakaoApiAddressResponse(
    val document: Document
) {
    class Document(
        val x: String,
        val y: String,
        val address: Address,
        val road_address: RoadAddress
    )

    class Address(
        val region_1depth_name: String,
        val region_2depth_name: String,
        val region_3depth_name: String,
    )

    class RoadAddress(
        val road_name: String,
        val h_code: String
    )
}
