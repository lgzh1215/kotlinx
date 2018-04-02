import groovy.lang.Closure
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
   kotlin("jvm")
   maven
}

apply {
   plugin("org.jetbrains.dokka")
}

dependencies {
   compile(kotlin("stdlib"))
   compile("org.slf4j:slf4j-api:1.7.25")
}

val javadocDir = tasks.withType<Javadoc>().first().destinationDir!!

tasks.withType<DokkaTask> {
   outputFormat = "html"
   outputDirectory = javadocDir.absolutePath
   reportUndocumented = false
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
