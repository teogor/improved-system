/*
 * Copyright 2023 teogor (Teodor Grigor)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
  alias(libs.plugins.gradle.publish)
  alias(libs.plugins.kotlin.serialization)
}

val javaVersion = JavaVersion.VERSION_1_8
java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  languageVersion = "1.8"
  jvmTarget = javaVersion.toString()
}

dependencies {
  api(project(":api"))

  // Xenoglot BoM
  api(platform(libs.xenoglot.bom))
  // Xenoglot Libraries
  api(libs.xenoglot.core)

  implementation(gradleApi())
  implementation(libs.android.gradlePlugin)
  implementation(libs.kotlin.gradlePlugin)
  implementation(libs.kotlin.xml.builder)
  implementation(libs.jdom2)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlin.poet)
}

gradlePlugin {
  plugins {
    register("querentPlugin") {
      id = "dev.teogor.querent"
      implementationClass = "dev.teogor.querent.Plugin"
    }
  }
}
