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

package dev.teogor.querent.utils

import org.gradle.api.GradleException
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import javax.inject.Inject

abstract class GitHashValueSource : ValueSource<String, ValueSourceParameters.None> {

  @get:Inject
  abstract val execOperations: ExecOperations

  // Define a variable to store the Git hash
  private var gitHash: String? = null

  override fun obtain(): String {
    // If gitHash is already calculated, return it
    gitHash?.let { return it }

    this.gitHash = try {
      val gitProcess = ProcessBuilder("git", "rev-parse", "HEAD").start()
      val outputReader = gitProcess.inputStream.bufferedReader()
      val gitHash = outputReader.readLine().trim()
      outputReader.close()
      gitProcess.waitFor()
      if (gitProcess.exitValue() == 0) {
        // git command executed successfully, store the hash
        gitHash
      } else {
        // git command failed, set gitHash to "N/A"
        "N/A"
      }
    } catch (e: GradleException) {
      "N/A"
    }

    return gitHash!!
  }
}
