## Demo problem to bind URL parameters to Collections

This project demonstrates the problem to bind Collection in Mictonaut microservice written in Kotlin.
It works unless the list of values in the URL is separated by slash "/". This also makes behavior inconsistent with URI Template FRC.

One can check examples in the tests. Failing tests have check on **shouldNotThrow\<Throable\>** instead of comparing to expected result.

I will try to reproduce this in Groovy and/or Java and will upload that here as well.
