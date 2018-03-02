import groovy.lang.Closure
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

buildscript {
    val kotlin_version: String by extra
    extra["kotlin_version"] = "1.2.30"

    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.16")
    }
}

group = "moe.aisia.kotlin"
version = "1.0"

plugins {
    java
}

apply {
    plugin("kotlin")
    plugin("org.jetbrains.dokka")
    plugin("maven")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val kotlin_version: String by extra
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    testCompile("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    testCompile("junit:junit:4.12")
    testCompile("io.kotlintest:kotlintest:2.0.7") {
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apply {
        suppressWarnings = true
        verbose = true
        freeCompilerArgs = listOf("-Xno-call-assertions",
                "-Xno-param-assertions", "-Xno-receiver-assertions")
    }
}

extensions.configure(KotlinJvmProjectExtension::class.java) {
    experimental.coroutines = Coroutines.ENABLE
}

val javadocDir = tasks.withType<Javadoc>().first().destinationDir!!

tasks.withType<DokkaTask> {
    outputFormat = "html"
    outputDirectory = javadocDir.absolutePath
    reportUndocumented = false

    linkMapping(delegateClosureOf<LinkMapping> {
        dir = "src/main/kotlin"
        url = "https://github.com/lgzh1215/kotlib/blob/master/src/main/kotlin"
        suffix = "#L"
    })
}

task<Jar>("sourcesJar") {
    dependsOn("classes")
    classifier = "sources"
    from(java.sourceSets["main"].allSource)
}

task<Jar>("javadocJar") {
    dependsOn("dokka")
    classifier = "javadoc"
    from(javadocDir)
}

artifacts {
    add("archives", tasks["sourcesJar"])
    add("archives", tasks["javadocJar"])
}
