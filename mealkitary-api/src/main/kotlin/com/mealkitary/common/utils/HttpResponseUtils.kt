package com.mealkitary.common.utils

import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

class HttpResponseUtils {

    companion object {

        fun createResourceUri(resourceId: Long) = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri()

        fun <T> mapToResponseEntity(emptiableList: List<T>): ResponseEntity<List<T>> {
            if (emptiableList.isEmpty()) {
                return ResponseEntity.noContent().build()
            }

            return ResponseEntity.ok(emptiableList)
        }
    }
}
