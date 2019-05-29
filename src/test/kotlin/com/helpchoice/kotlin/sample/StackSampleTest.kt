package com.helpchoice.kotlin.sample

import io.kotlintest.Spec
import io.kotlintest.TestCaseOrder
import io.kotlintest.shouldBe
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
            val response: String = client.toBlocking().retrieve("/repeat/1/2.3/4.0")
            response shouldBe ""
        }

        "test single value" {
            var response: String = client.toBlocking().retrieve("/repeat/2")
            response shouldBe ""

            response = client.toBlocking().retrieve("/repeat/2.3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/repeat/-3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/repeat/-3.2")
            response shouldBe ""

            response = client.toBlocking().retrieve("/repeat/0")
            response shouldBe ""

            response = client.toBlocking().retrieve("/repeat/2.34567")
            response shouldBe ""
        }

        "test {+strings}" {
            var response: String = client.toBlocking().retrieve("/strPlus/2")
            response shouldBe ""

            response = client.toBlocking().retrieve("/strPlus/2/3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/strPlus/-2/-3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/strPlus/2.3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/strPlus/2/3/4.567")
            response shouldBe ""

            response = client.toBlocking().retrieve("/strPlus/2/3/4.567/8")
            response shouldBe ""
        }

        "test {/strings}" {
            var response: String = client.toBlocking().retrieve("/str/2")
            response shouldBe ""

            response = client.toBlocking().retrieve("/str/2/3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/str/-2/-3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/str/2.3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/str/2/3/4.567")
            response shouldBe ""

            response = client.toBlocking().retrieve("/str/2/3/4.567/8")
            response shouldBe ""
        }

        "test {/int} Collection" {
            var response: String = client.toBlocking().retrieve("/int/2")
            response shouldBe ""

            response = client.toBlocking().retrieve("/int/2/3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/int/-2/-3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/int/2.3")
            response shouldBe ""

            response = client.toBlocking().retrieve("/int/2/3,4.567")
            response shouldBe ""

            response = client.toBlocking().retrieve("/int/2/3,4.567/8")
            response shouldBe ""
        }
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)

        client.close()
        embeddedServer.close()
    }
}
