package com.mealkitary.paymentgateway.codec

import com.mealkitary.paymentgateway.TossPaymentCodec
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class UrlSafeBase64Codec : TossPaymentCodec {

    override fun encode(input: String): String {
        val urlSafeEncoder = Base64.getUrlEncoder()
        val urlSafeEncodedBytes = urlSafeEncoder.encode(input.toByteArray())

        return String(urlSafeEncodedBytes)
    }

    override fun decode(input: String): String {
        val urlSafeDecoder = Base64.getUrlDecoder()
        val urlSafeDecodedBytes = urlSafeDecoder.decode(input)

        return String(urlSafeDecodedBytes)
    }
}
