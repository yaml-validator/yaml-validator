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

internal class TestValidatorNN {
  @Test
  internal fun `should pass when top level object is validatable`() {
    val toValidate = Yaml().loadAs("name: abc", Map::class.java)
    YamlValidator.from("name: !nn")
        .ignoreMissing()
        .build()
        .validate(toValidate)
  }

  @Test
  internal fun `should pass when nut null`() {
    val toValidate = Yaml().loadAs("""
      students:
        - name: abc
          age: 23
        - name: abc
          age: 23
    """.trimIndent(), Map::class.java)
    YamlValidator.from(yamlInputStream)
        .ignoreMissing()
        .build()
        .validate(toValidate)
  }

  @Test
  internal fun `should fail when null`() {
    assertThrows<ValidateException> {
      val toValidate = Yaml().loadAs("""
        students:
          - name: ~
            age: 23
          - name: ~
            age: "23"
      """.trimIndent(), Map::class.java)
      YamlValidator.from(yamlInputStream)
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }
  }

  @Test
  internal fun `should fail when not equal to anchor`() {
    assertThrows<ValidateException> {
      val toValidate = Yaml().loadAs("""
        students:
          - name: whatever
            age: 23
          - name: whatever
            age: 24
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
        student:
          name: whatever
          age: 23
      """.trimIndent(), Map::class.java)
      YamlValidator.from(yamlInputStream)
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }

    assertThrows<ValidateException> {
      val toValidate = Yaml().loadAs("""
        student: ~
      """.trimIndent(), Map::class.java)
      YamlValidator.from(yamlInputStream)
          .ignoreMissing()
          .build()
          .validate(toValidate)
    }
  }

  companion object {
    private val yamlInputStream get() = TestValidatorNN::class.java.getResourceAsStream("/nn.v.yaml")
  }
}
