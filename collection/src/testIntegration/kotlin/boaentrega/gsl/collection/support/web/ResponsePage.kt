package boaentrega.gsl.collection.support.web

import boaentrega.gsl.collection.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.collection.support.extensions.ClassExtensions.toObject
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class ResponsePage : PageImpl<String> {
    @JsonCreator
    constructor(@JsonProperty("content") content: List<Any>,
                @JsonProperty("number") number: Int,
                @JsonProperty("size") size: Int,
                @JsonProperty("totalElements") totalElements: Long,
                @JsonProperty("pageable") pageable: JsonNode?,
                @JsonProperty("last") last: Boolean,
                @JsonProperty("totalPages") totalPages: Int,
                @JsonProperty("sort") sort: JsonNode?,
                @JsonProperty("first") first: Boolean,
                @JsonProperty("numberOfElements") numberOfElements: Int,
                @JsonProperty("empty") empty: Boolean) : super(
            content.map { it.toJsonString() }, PageRequest.of(number, size), totalElements) {
    }

    inline fun <reified T> getObject(): List<T> {
        return content.map { it.toObject() as T }
    }
}