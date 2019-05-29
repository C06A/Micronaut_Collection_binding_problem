package com.helpchoice.kotlin.sample

import io.kotlintest.Spec
import io.kotlintest.TestCaseOrder
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import io.kotlintest.specs.StringSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer

internal class StackSampleTest : StringSpec() {
    override fun testCaseOrder(): TestCaseOrder? = TestCaseOrder.Random

    lateinit var embeddedServer: EmbeddedServer
    lateinit var client: HttpClient

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)

        embeddedServer = ApplicationContext.run(EmbeddedServer::class.java, mapOf("port" to -1))
        client = HttpClient.create(embeddedServer.url)
    }

    init {
        "test stack" {
            client.toBlocking().retrieve("/repeat/1/2.3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/1/2.3\",\"type\":\"application/hal+json\"}},\"stack\":[2.3],\"value\":1}"

            client.toBlocking().retrieve("/repeat/1/2.3,4.0") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/1/2.3/4.0\",\"type\":\"application/hal+json\"}},\"stack\":[2.3,4.0],\"value\":1}"

            client.toBlocking().retrieve("/repeat/1/2.3,4.0,-12") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/1/2.3/4.0/-12\",\"type\":\"application/hal+json\"}},\"stack\":[2.3,4.0,-12],\"value\":1}"

            shouldNotThrow<Throwable> { client.toBlocking().retrieve("/repeat/1/2.3/4.0") }
        }

        "test single value" {
            client.toBlocking().retrieve("/repeat/2") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2\",\"type\":\"application/hal+json\"}},\"value\":2}"

            client.toBlocking().retrieve("/repeat/2.3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2.3\",\"type\":\"application/hal+json\"}},\"value\":2.3}"

            client.toBlocking().retrieve("/repeat/-3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/-3\",\"type\":\"application/hal+json\"}},\"value\":-3}"

            client.toBlocking().retrieve("/repeat/-3.2") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/-3.2\",\"type\":\"application/hal+json\"}},\"value\":-3.2}"

            client.toBlocking().retrieve("/repeat/0") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/0\",\"type\":\"application/hal+json\"}},\"value\":0}"

            client.toBlocking().retrieve("/repeat/2.34567") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2.34567\",\"type\":\"application/hal+json\"}},\"value\":2.34567}"
        }

        "test {+strings}" {

            client.toBlocking().retrieve("/strPlus/2/3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3\",\"type\":\"application/hal+json\"}},\"stack\":[3],\"value\":2}"

            client.toBlocking().retrieve("/strPlus/-2/-3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/-2/-3\",\"type\":\"application/hal+json\"}},\"stack\":[-3],\"value\":-2}"

            client.toBlocking().retrieve("/strPlus/2/3/4.567") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3/4.567\",\"type\":\"application/hal+json\"}},\"stack\":[3,4.567],\"value\":2}"

            client.toBlocking().retrieve("/strPlus/2/3/4.567/8") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3/4.567/8\",\"type\":\"application/hal+json\"}},\"stack\":[3,4.567,8],\"value\":2}"

            // there is no way to make second parameter optional

//            client.toBlocking().retrieve("/strPlus/2") shouldBe ""

//            client.toBlocking().retrieve("/strPlus/2.3") shouldBe ""
        }

        "test {/strings}" {
            client.toBlocking().retrieve("/str/2") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2\",\"type\":\"application/hal+json\"}},\"value\":2}"

            client.toBlocking().retrieve("/str/2/3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3\",\"type\":\"application/hal+json\"}},\"stack\":[3],\"value\":2}"

            client.toBlocking().retrieve("/str/-2/-3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/-2/-3\",\"type\":\"application/hal+json\"}},\"stack\":[-3],\"value\":-2}"

            client.toBlocking().retrieve("/str/2.3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2.3\",\"type\":\"application/hal+json\"}},\"value\":2.3}"

            client.toBlocking().retrieve("/str/2/3/4.567") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3/4.567\",\"type\":\"application/hal+json\"}},\"stack\":[3,4.567],\"value\":2}"

            client.toBlocking().retrieve("/str/2/3/4.567/8") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3/4.567/8\",\"type\":\"application/hal+json\"}},\"stack\":[3,4.567,8],\"value\":2}"
        }

        "test {/int} Collection" {
            client.toBlocking().retrieve("/int/2") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2\",\"type\":\"application/hal+json\"}},\"value\":2}"

            client.toBlocking().retrieve("/int/2/3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3\",\"type\":\"application/hal+json\"}},\"stack\":[3],\"value\":2}"

            client.toBlocking().retrieve("/int/-2/-3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/-2/-3\",\"type\":\"application/hal+json\"}},\"stack\":[-3],\"value\":-2}"

            client.toBlocking().retrieve("/int/2.3") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2.3\",\"type\":\"application/hal+json\"}},\"value\":2.3}"

            client.toBlocking().retrieve("/int/2/3,4.567") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3/4.567\",\"type\":\"application/hal+json\"}},\"stack\":[3,4.567],\"value\":2}"

            client.toBlocking().retrieve("/int/2/3,4.567,8") shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3/4.567/8\",\"type\":\"application/hal+json\"}},\"stack\":[3,4.567,8],\"value\":2}"

            shouldNotThrow<Throwable> { client.toBlocking().retrieve("/int/2/3/4.567/8") } // shouldBe "{\"_links\":{\"self\":{\"href\":\"/repeat/2/3/4.567/8\",\"type\":\"application/hal+json\"}},\"stack\":[3,4.567,8],\"value\":2}"
        }
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)

        client.close()
        embeddedServer.close()
    }
}
