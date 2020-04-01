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
import com.github.kezhenxu94.exceptions.ValidateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.yaml.snakeyaml.Yaml

class TestValidatorLT {
  @Test
  fun `should pass when less than`() {
    val toValidate = Yaml().loadAs("""
      students:
        - name: whatever
          age: 11
        - name: whatever
          age: "11"
    """.trimIndent(), Map::class.java)
    YamlValidator.from(javaClass.getResourceAsStream("/lt.v.yaml"))
        .ignoreMissing()
        .build()
        .validate(toValidate)
  }

  @Test
  fun `should fail when not less than`() {
    assertThrows<ValidateException> {
      val toValidate = Yaml().loadAs("""
        students:
          - name: whatever
            age: 12
          - name: whatever
            age: 12
      """.trimIndent(), Map::class.java)
      YamlValidator.from(javaClass.getResourceAsStream("/lt.v.yaml"))
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }
  }

  @Test
  fun `should fail when not equal to anchor`() {
    assertThrows<ValidateException> {
      val toValidate = Yaml().loadAs("""
          students:
            - name: whatever
              age: 11
            - name: whatever
              age: 10
        """.trimIndent(), Map::class.java)
      YamlValidator.from(javaClass.getResourceAsStream("/lt.v.yaml"))
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }
  }
}
