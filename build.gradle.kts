import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.31"
    application
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("com.github.johnrengelman.shadow") version "4.0.2"
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
}

version = "0.1"
group = "com.helpchoice.kotlin.sample"

val kotlinVersion: String by ext
val spekVersion: String by ext

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jcenter.bintray.com")
}

dependencyManagement {
    imports {
        mavenBom("io.micronaut:micronaut-bom:1.1.1")
    }
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    compile("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    compile("io.micronaut:micronaut-runtime")
    compile("io.micronaut:micronaut-http-client")
    compile("io.micronaut:micronaut-http-server-netty")
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    kaptTest("io.micronaut:micronaut-inject-java")
    runtime("ch.qos.logback:logback-classic:1.2.3")
    runtime("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4.1")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.1.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.1.0")

//    testCompile("org.jetbrains.spek:spek-api:${spekVersion}")
//    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:${spekVersion}")
//    testCompile("org.spekframework.spek2:spek-dsl-jvm:${spekVersion}")
//    testRuntime("org.spekframework.spek2:spek-runner-junit5:${spekVersion}")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.0")

    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testCompile("io.micronaut.test:micronaut-test-junit5:1.0.4")
    testCompile("org.mockito:mockito-junit-jupiter:2.22.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.1.0")
}

allOpen {
    annotation("io.micronaut.aop.Around")
}

application.mainClassName = "com.helpchoice.kotlin.sample.Application"

val shadowJar: ShadowJar by tasks
shadowJar.mergeServiceFiles()

val run: JavaExec by tasks
run.jvmArgs("-noverify", "-XX:TieredStopAtLevel=1")

val test: Test by tasks
test.useJUnitPlatform {
    includeEngines("spek", "spek2", "some-other-test-engine")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        //Will retain parameter names for Java reflection
        javaParameters = true
    }
}

val jar: Jar by tasks
jar.manifest.attributes(
        mapOf(
                "Main-Class" to application.mainClassName
        )
)
