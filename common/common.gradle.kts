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
   testCompile("junit:junit:4.12")
   testCompile(kotlin("reflect"))
   testCompile("io.kotlintest:kotlintest:2.0.7") {
      exclude("org.jetbrains.kotlin", "kotlin-reflect")
   }
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
