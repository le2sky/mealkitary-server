package com.mealkitary.address

import com.mealkitary.address.payload.KakaoApiAddressResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory

@Component
class KakaoApiWebClient(
    private val webClient: WebClient,
    @Value("\${kakaoapi.address.serviceKey}")
    private val serviceKey: String,
) {

    fun requestAddress(query: String, baseUrl: String): KakaoApiAddressResponse {
        val kakaoApiAddressResponse = webClient
            .mutate()
            .uriBuilderFactory(DefaultUriBuilderFactory())
            .build()
            .get()
            .uri("$baseUrl/v2/local/search/address.json?query=$query")
            .header("Authorization", "KakaoAK $serviceKey")
            .retrieve()
            .bodyToMono(KakaoApiAddressResponse::class.java)
            .block()

        return kakaoApiAddressResponse!!
    }
}
