package com.mealkitary.common.utils

import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.UUID

class HttpResponseUtils {

    companion object {

        fun createResourceUri(resourceId: UUID): URI {
            val uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().build()
            val scheme = removeSlash(uriComponents.scheme)
            val host = removeSlash(uriComponents.host)
            val path = removeSlash(uriComponents.path)

            return URI.create("$scheme://$host/$path/$resourceId")
        }

        fun createResourceUri(path: String, resourceId: UUID): URI {
            val uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().build()
            val scheme = removeSlash(uriComponents.scheme)
            val host = removeSlash(uriComponents.host)

            return URI.create("$scheme://$host/$path/$resourceId")
        }

        private fun removeSlash(str: String?) = str?.replace("/", "")

        fun <T> mapToResponseEntity(emptiableList: List<T>): ResponseEntity<List<T>> {
            if (emptiableList.isEmpty()) {
                return ResponseEntity.noContent().build()
            }

            return ResponseEntity.ok(emptiableList)
        }
    }
}
