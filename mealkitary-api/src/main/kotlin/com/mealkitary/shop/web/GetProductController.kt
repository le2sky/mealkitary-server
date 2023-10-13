package com.mealkitary.shop.web

import com.mealkitary.common.utils.HttpResponseUtils
import com.mealkitary.common.utils.UUIDUtils
import com.mealkitary.shop.application.port.input.GetProductQuery
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
    fun getAllProductOfShop(@PathVariable("shopId") shopId: String) =
        HttpResponseUtils.mapToResponseEntity(
            emptiableList = getProductQuery.loadAllProductByShopId(UUIDUtils.fromString(shopId))
        )
}
