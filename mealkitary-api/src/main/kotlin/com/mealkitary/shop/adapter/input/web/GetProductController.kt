package com.mealkitary.shop.adapter.input.web

import com.mealkitary.shop.application.port.input.GetProductQuery
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shops")
class GetProductController(
    private val getProductQuery: GetProductQuery
) {

    @GetMapping("/{shopId}/products")
    fun getAllProductOfShop(@PathVariable("shopId") shopId: Long) =
        ResponseEntity.ok(getProductQuery.loadAllProductByShopId(shopId))
}
