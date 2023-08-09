package com.mealkitary.reservation.adapter.output.paymentgateway.codec

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class UrlSafeBase64CodecTest : AnnotationSpec() {

    @Test
    fun `url safe base64로 인코딩한다`() {
        val urlSafeBase64Codec = UrlSafeBase64Codec()
        val source = "encodeTargetStringSample"

        val result = urlSafeBase64Codec.encode(source)

        result shouldBe "ZW5jb2RlVGFyZ2V0U3RyaW5nU2FtcGxl"
    }

    @Test
    fun `url safe base64로 디코딩한다`() {
        val urlSafeBase64Codec = UrlSafeBase64Codec()
        val source = "ZW5jb2RlVGFyZ2V0U3RyaW5nU2FtcGxl"

        val result = urlSafeBase64Codec.decode(source)

        result shouldBe "encodeTargetStringSample"
    }
}
