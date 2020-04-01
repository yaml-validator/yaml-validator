/**
 * Copyright 2020 kezhenxu94
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.kezhenxu94.validators.lt

import io.github.kezhenxu94.Validatable
import io.github.kezhenxu94.annotations.Validator
import io.github.kezhenxu94.exceptions.ValidateException

@Validator(tags = ["!lt"], construct = LessThanConstruct::class)
internal class LessThanValidator(private val expected: Double = 0.0) : Validatable {
  private var actual: Double? = null

  @Throws(ValidateException::class)
  override fun validate(any: Any?) {
    val actual = when (any) {
      is Number -> any.toDouble()
      is String -> any.toDouble()
      else      -> throw ValidateException("!lt validator cannot be applied to non-number values, actual: $any")
    }

    if (this.actual == null) {
      validateAnchor(actual.also { this.actual = it })
    } else {
      validateAlias(actual)
    }
  }

  private fun validateAnchor(anchor: Number) {
    if (anchor.toDouble() >= expected) {
      throw ValidateException()
    }
  }

  private fun validateAlias(alias: Number) {
    if (alias.toDouble() != actual) {
      throw ValidateException()
    }
  }
}
