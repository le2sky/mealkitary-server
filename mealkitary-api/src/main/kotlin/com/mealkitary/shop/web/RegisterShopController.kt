package com.mealkitary.shop.web

import com.mealkitary.common.utils.HttpResponseUtils
import com.mealkitary.shop.application.port.input.RegisterShopUseCase
import com.mealkitary.shop.web.request.RegisterShopWebRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/shops")
class RegisterShopController(
    private val registerShopUseCase: RegisterShopUseCase
) {

    @PostMapping
    fun registerShop(@Valid @RequestBody registerShopWebRequest: RegisterShopWebRequest): ResponseEntity<Unit> {
        val resourceId = registerShopUseCase.register(registerShopWebRequest.mapToServiceRequest())
        val location = HttpResponseUtils.createResourceUri(resourceId)

        return ResponseEntity.created(location).build()
    }
}
