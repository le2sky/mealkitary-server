package com.mealkitary.common.utils

import org.springframework.web.servlet.support.ServletUriComponentsBuilder

class HttpResponseUtils {

    companion object {

        fun createResourceUri(resourceId: Long) = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri()
    }
}
