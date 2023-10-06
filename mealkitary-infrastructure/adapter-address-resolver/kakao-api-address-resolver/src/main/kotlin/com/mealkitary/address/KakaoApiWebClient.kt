package com.mealkitary.address

import com.mealkitary.address.payload.KakaoApiAddressResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

private const val KAKAO_API_BASE_URL = "/v2/local/search/address"
private const val FORMAT = "json"

@Component
class KakaoApiWebClient(
    private val webClient: WebClient,
    @Value("\${kakaoapi.address.serviceKey}")
    private val serviceKey: String,
) {

    fun requestAddress(query: String): KakaoApiAddressResponse {
        val kakaoApiAddressResponse = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("$KAKAO_API_BASE_URL.$FORMAT")
                    .queryParam("query", query)
                    .build()
            }
            .header("Authorization", "KakaoAK $serviceKey")
            .retrieve()
            .bodyToMono(KakaoApiAddressResponse::class.java)
            .block()

        return kakaoApiAddressResponse!!
    }
}
