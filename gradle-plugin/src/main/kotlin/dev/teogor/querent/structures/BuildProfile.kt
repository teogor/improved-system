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

package dev.teogor.querent.structures

import com.android.build.api.variant.Variant
import com.android.build.gradle.AppExtension
import dev.teogor.querent.api.codegen.Blueprint
import dev.teogor.querent.api.codegen.FoundationData
import dev.teogor.querent.api.impl.QuerentConfiguratorExtension
import dev.teogor.querent.api.models.PackageDetails
import dev.teogor.querent.tasks.GenerateBuildProfileFileTask
import dev.teogor.querent.tasks.GenerateBuildTypesTask
import dev.teogor.querent.utils.ceresBomDependency
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

class BuildProfile(data: FoundationData) : Blueprint(data) {

  private val appExtension by lazy { extension<AppExtension>()!! }
  private val buildOptions by lazy { extension<QuerentConfiguratorExtension>()!! }
  private val buildFeatures by lazy { buildOptions.buildFeatures }

  override val packageNameSuffix: String
    get() = "build"

  override fun isEnabled() = buildFeatures.buildProfile

  override fun onVariants(variant: Variant) {
    super.onVariants(variant)

    val kotlinSources = kotlinSources

    val packageDetails = PackageDetails(
      packageName = packageName,
      namespace = namespace,
    )

    val taskProvider = project.tasks.register<GenerateBuildProfileFileTask>(
      "generateBuildProfile${variant.name.capitalized()}",
    ) {
      debug.set(appExtension.buildTypes.getByName(variant.name).isDebuggable)
      packageName.set(this@BuildProfile.packageName)
      this.buildType.set(variant.name)
      ceresDependency.set(project.ceresBomDependency()?.version)
      packageId.set(namespace)
      versionName.set(appExtension.defaultConfig.versionName ?: "n/a")
      versionCode.set(appExtension.defaultConfig.versionCode?.toLong() ?: 0)
      this.packageDetails.set(packageDetails)
      outputDir.set(kotlinSources)
      doLast {
        println("Git hash: setting???")
        val gitProcess = ProcessBuilder("git", "rev-parse", "HEAD").start()
        val outputReader = gitProcess.inputStream.bufferedReader()
        val gitHash = outputReader.readLine().trim()
        outputReader.close()
        gitProcess.waitFor()

        if (gitProcess.exitValue() == 0) {
          println("Git hash: $gitHash")
        } else {
          println("Failed to get Git hash")
        }
        gitHashProvider.set(gitHash) // project.providers.of(GitHashValueSource::class) {}.get())
      }
    }

    val taskBuildTypesTask = project.tasks.register<GenerateBuildTypesTask>(
      "generateBuildProfileTypes${variant.name.capitalized()}",
    ) {
      packageName.set(this@BuildProfile.packageName)
      this.buildTypes.set(appExtension.buildTypes.map { it.name })
      this.packageDetails.set(packageDetails)
      outputDir.set(kotlinSources)
    }

    project.afterEvaluate {
      project.tasks["pre${variant.name.capitalized()}Build"].apply {
        dependsOn(taskProvider)
        dependsOn(taskBuildTypesTask)
      }
    }
  }
}
