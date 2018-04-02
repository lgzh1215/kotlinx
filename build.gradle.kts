import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
   repositories {
      mavenCentral()
      jcenter()
   }

   dependencies {
      val dokkaVersion = "0.9.16"
      classpath("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
   }
}

plugins {
   kotlin("jvm") version "1.2.31" apply false
}

allprojects {
   group = "moe.aisia.kotlinx"
   version = "1.0"

   repositories {
      mavenLocal()
      mavenCentral()
      jcenter()
   }

   tasks.withType<JavaCompile> {
      options.encoding = "UTF-8"
   }

   tasks.withType<KotlinCompile> {
      kotlinOptions {
         suppressWarnings = true
         verbose = true
         freeCompilerArgs = listOf(
               "-Xno-call-assertions",
               "-Xno-param-assertions",
               "-Xno-receiver-assertions",
               "-Xeffect-system",
               "-Xread-deserialized-contracts"
         )
      }
   }
}
