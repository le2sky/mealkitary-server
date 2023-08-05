package com.mealkitary.shop.adapter.input.web

import com.mealkitary.common.utils.HttpResponseUtils
import com.mealkitary.shop.application.port.input.GetShopQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shops")
class GetShopController(
    private val getShopQuery: GetShopQuery
) {

    @GetMapping
    fun getAllShop() =
        HttpResponseUtils.mapToResponseEntity(emptiableList = getShopQuery.loadAllShop())
}
