package com.mealkitary.address.payload

data class KakaoApiAddressResponse(
    val document: Document
) {
    data class Document(
        val x: String,
        val y: String,
        val address: Address,
        val road_address: RoadAddress
    )

    data class Address(
        val region_1depth_name: String,
        val region_2depth_name: String,
        val region_3depth_name: String,
    )

    data class RoadAddress(
        val road_name: String,
        val h_code: String
    )
}
