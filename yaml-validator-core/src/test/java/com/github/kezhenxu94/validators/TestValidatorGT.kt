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

package com.github.kezhenxu94.validators

import com.github.kezhenxu94.YamlValidator
import com.github.kezhenxu94.exception.ValidateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.yaml.snakeyaml.Yaml

class TestValidatorGT {
  @Test
  fun shouldPass() {
    val toValidate: Map<*, *> = Yaml().loadAs<Map<*, *>>("""
      students:
        - name: whatever
          age: 23
    """.trimIndent(), Map::class.java)
    YamlValidator.from(javaClass.getResourceAsStream("/nn.v.yaml"))
        .ignoreMissing()
        .build()
        .validate(toValidate)
  }

  @Test
  fun shouldFail() {
    assertThrows<ValidateException> {
      val toValidate: Map<*, *> = Yaml().loadAs<Map<*, *>>("""
      students:
        - name: whatever
          age: 10
    """.trimIndent(), Map::class.java)
      YamlValidator.from(javaClass.getResourceAsStream("/nn.v.yaml"))
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }
  }
}
