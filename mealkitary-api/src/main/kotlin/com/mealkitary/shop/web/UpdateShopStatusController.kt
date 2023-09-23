package com.mealkitary.shop.web

import com.mealkitary.shop.application.port.input.UpdateShopStatusUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shops")
class UpdateShopStatusController(
    private val updateShopStatusUseCase: UpdateShopStatusUseCase
) {

    @PostMapping("/{shopId}/status")
    fun updateShopStatus(@PathVariable("shopId") shopId: Long): ResponseEntity<Unit> {
        updateShopStatusUseCase.update(shopId)

        return ResponseEntity.noContent().build()
    }
}
