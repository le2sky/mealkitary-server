package com.mealkitary.shop.domain.shop.address

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Address(
    @Column(name = "region_1depth_name", nullable = false)
    val region1DepthName: String,
    @Column(name = "region_2depth_name", nullable = false)
    val region2DepthName: String,
    @Column(name = "region_3depth_name")
    val region3DepthName: String,
    @Column(name = "road_name")
    val roadName: String
) {

    companion object {
        fun of(
            region1DepthName: String,
            region2DepthName: String,
            region3DepthName: String,
            roadName: String
        ): Address {

            return Address(
                region1DepthName,
                region2DepthName,
                region3DepthName,
                roadName
            )
        }
    }
}
