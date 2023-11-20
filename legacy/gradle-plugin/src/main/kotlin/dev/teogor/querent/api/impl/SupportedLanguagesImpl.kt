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

package dev.teogor.querent.api.impl

import dev.teogor.xenoglot.LanguageFamily
import dev.teogor.querent.api.SupportedLanguages

class SupportedLanguagesImpl : SupportedLanguages {
  private val languages = mutableListOf<LanguageFamily>()

  override operator fun LanguageFamily.unaryPlus() {
    languages.add(this)
  }

  override fun getLanguages(): Array<LanguageFamily> {
    return languages.toTypedArray()
  }
}