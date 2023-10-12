package com.mealkitary.address.payload

data class KakaoApiAddressResponse(
    val documents: List<Document>,
    val meta: Meta
)

data class Meta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)
data class Document(
    val address_name: String,
    val address_type: String,
    val address: Address?,
    val road_address: RoadAddress?,
    val x: String,
    val y: String
)

class Address(
    val address_name: String,
    val b_code: String,
    val h_code: String,
    val main_address_no: String,
    val mountain_yn: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_h_name: String,
    val region_3depth_name: String,
    val sub_address_no: String,
    val x: String,
    val y: String

)

class RoadAddress(
    val address_name: String,
    val building_name: String,
    val main_building_no: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val road_name: String,
    val sub_building_no: String,
    val underground_yn: String,
    val x: String,
    val y: String,
    val zone_no: String
)

/**
 *{
 *     "documents": [
 *         {
 *             "address": {
 *                 "address_name": "경기 안양시 만안구 안양동 756-15",
 *                 "b_code": "4117110100",
 *                 "h_code": "4117158100",
 *                 "main_address_no": "756",
 *                 "mountain_yn": "N",
 *                 "region_1depth_name": "경기",
 *                 "region_2depth_name": "안양시 만안구",
 *                 "region_3depth_h_name": "안양9동",
 *                 "region_3depth_name": "안양동",
 *                 "sub_address_no": "15",
 *                 "x": "126.909658990799",
 *                 "y": "37.3921505309817"
 *             },
 *             "address_name": "경기 안양시 만안구 병목안로 129",
 *             "address_type": "ROAD_ADDR",
 *             "road_address": {
 *                 "address_name": "경기 안양시 만안구 병목안로 129",
 *                 "building_name": "",
 *                 "main_building_no": "129",
 *                 "region_1depth_name": "경기",
 *                 "region_2depth_name": "안양시 만안구",
 *                 "region_3depth_name": "안양동",
 *                 "road_name": "병목안로",
 *                 "sub_building_no": "",
 *                 "underground_yn": "N",
 *                 "x": "126.909658990799",
 *                 "y": "37.3921505309817",
 *                 "zone_no": "14023"
 *             },
 *             "x": "126.909658990799",
 *             "y": "37.3921505309817"
 *         }
 *     ],
 *     "meta": {
 *         "is_end": true,
 *         "pageable_count": 1,
 *         "total_count": 1
 *     }
 * }
 *
 *
 *
 */
