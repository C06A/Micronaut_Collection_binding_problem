package com.helpchoice.kotlin.sample

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.uri.UriTemplate
import java.math.BigDecimal

const val REPEAT_WITH_STACK = "/repeat/{value}{/stack*}"

@Controller("/", produces = [MediaType.APPLICATION_HAL_JSON])
class StackSample {

    // @Get("/repeat/{value}{/stack*}")
    // fun collectionStar(value: Number, stack: List<Number>): Map<String, Any?>
    @Get(REPEAT_WITH_STACK)
    fun collectionStar(value: BigDecimal, stack: Collection<BigDecimal>?): Map<String, Any?> {
        val selfHref = UriTemplate(REPEAT_WITH_STACK).expand(mapOf(
                "value" to value
                , "stack" to stack
        ))

        return mapOf(
                "_links" to mapOf(
                        "self" to mapOf(
                                "href" to selfHref,
                                "type" to MediaType.APPLICATION_HAL_JSON
                        )
                ),
                "stack" to stack,
                "value" to value
        )
    }

    @Get("/numRepeat/{value}{/stack*}")
    fun strCollectionStar(value: Number, stack: Collection<Number>?): Map<String, Any?> {
        val selfHref = UriTemplate(REPEAT_WITH_STACK).expand(mapOf(
                "value" to value
                , "stack" to stack
        ))

        return mapOf(
                "_links" to mapOf(
                        "self" to mapOf(
                                "href" to selfHref,
                                "type" to MediaType.APPLICATION_HAL_JSON
                        )
                ),
                "stack" to stack,
                "value" to value
        )
    }

    @Get("/repeat/{value}")
    fun valueOnly(value: BigDecimal): Map<String, Any?> {
        return collectionStar(value, null)
    }

    @Get("/strPlus/{value}/{+stack:[-0-9\\./]+}")
    fun strPlus(value: String, stack: String): Map<String, Any?> {
        return collectionStar(value.toBigDecimal(), stack.split("/").map { it.toBigDecimal() })
    }

    @Get("/str/{value}{/stack:[-0-9\\./]+}")
    fun str(value: String, stack: String?): Map<String, Any?> {
        return collectionStar(value.toBigDecimal(), stack?.split("/")?.map { it.toBigDecimal() })
    }

    @Get("/int/{value}{/stack}")
    fun intComma(value: BigDecimal, stack: Collection<BigDecimal>?): Map<String, Any?> {
        return collectionStar(value, stack)
    }
}
