package com.helpchoice.kotlin.sample

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.helpchoice.kotlin.sample")
                .mainClass(Application.javaClass)
                .start()
    }
}