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

package io.github.kezhenxu94.validators

import io.github.kezhenxu94.YamlValidator
import io.github.kezhenxu94.exceptions.ValidateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.yaml.snakeyaml.Yaml

internal class TestTagProcessorGT {
  @Test
  internal fun `should pass when gt`() {
    val toValidate = Yaml().loadAs("""
      students:
        - name: whatever
          age: 23
        - name: whatever
          age: "23"
    """.trimIndent(), Map::class.java)
    YamlValidator.from(yamlInputStream)
        .ignoreMissing()
        .build()
        .validate(toValidate)
  }

  @Test
  internal fun `should fail when not gt`() {
    assertThrows<ValidateException> {
      val toValidate = Yaml().loadAs("""
        students:
          - name: whatever
            age: 10
      """.trimIndent(), Map::class.java)
      YamlValidator.from(yamlInputStream)
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }
  }

  @Test
  internal fun `should fail when type mismatch`() {
    assertThrows<ValidateException> {
      val toValidate = Yaml().loadAs("""
        students:
          - name: whatever
            age: true
      """.trimIndent(), Map::class.java)
      YamlValidator.from(yamlInputStream)
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }
  }

  @Test
  internal fun `should pass with POJO`() {
    val toValidate = mapOf("students" to listOf(
        Student(name = "whatever", age = 23),
        Student(name = "whatever", age = 23)
    ))

    YamlValidator.from(yamlInputStream)
        .ignoreMissing()
        .build()
        .validate(toValidate)
  }

  companion object {
    private val yamlInputStream get() = TestTagProcessorGT::class.java.getResourceAsStream("/nn.v.yaml")
  }
}
