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

internal class TestTagProcessorEQ {
    @Test
    internal fun `should pass when eq`() {
        val toValidate = Yaml().loadAs(
            """
            students:
              - name: whatever
                age: 12
              - name: whatever
                age: "12"
            """.trimIndent(),
            Map::class.java
        )
        YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .build()
            .validate(toValidate)
    }

    @Test
    internal fun `should fail when not eq`() {
        assertThrows<ValidateException> {
            val toValidate = Yaml().loadAs(
                """
                students:
                  - name: whatever
                    age: 11
                  - name: whatever
                    age: 11
                """.trimIndent(),
                Map::class.java
            )
            YamlValidator.from(yamlInputStream)
                .ignoreMissing()
                .build()
                .validate(toValidate)
        }
    }

    @Test
    internal fun `should fail when not eq to anchor`() {
        val validator = YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .build()

        assertThrows<ValidateException> {
            validator.validate(
                Yaml().loadAs(
                    """
                    students:
                      - name: whatever
                        age: 12
                      - name: whatever
                        age: 19
                    """.trimIndent(),
                    Map::class.java
                )
            )
        }
    }

    @Test
    internal fun `should pass when eq to anchor`() {
        val validator = YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .build()

        assertThrows<ValidateException> {
            validator.validate(
                Yaml().loadAs(
                    """
                    students:
                      - name: whatever
                        age: 11
                      - name: whatever
                        age: 19
                    """.trimIndent(),
                    Map::class.java
                )
            )
        }

        validator.validate(
            Yaml().loadAs(
                """
                students:
                  - name: whatever
                    age: 11
                  - name: whatever
                    age: 11
                """.trimIndent(),
                Map::class.java
            )
        )
    }

    @Test
    internal fun `should pass when reset and not eq to anchor`() {
        val validator = YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .disableReference()
            .build()

        assertThrows<ValidateException> {
            validator.validate(
                Yaml().loadAs(
                    """
                    students:
                      - name: whatever
                        age: 11
                      - name: whatever
                        age: 19
                    """.trimIndent(),
                    Map::class.java
                )
            )
        }

        assertThrows<ValidateException> {
            validator.validate(
                Yaml().loadAs(
                    """
                    students:
                      - name: whatever
                        age: 11
                      - name: whatever
                        age: 11
                    """.trimIndent(),
                    Map::class.java
                )
            )
        }
    }

    @Test
    internal fun `should fail when type mismatch`() {
        assertThrows<ValidateException> {
            val toValidate = Yaml().loadAs(
                """
                students:
                  - name: whatever
                    age: true
                """.trimIndent(),
                Map::class.java
            )
            YamlValidator.from(yamlInputStream)
                .ignoreMissing()
                .build()
                .validate(toValidate)
        }
    }

    @Test
    internal fun `should pass with POJO`() {
        val toValidate = mapOf(
            "students" to listOf(
                Student(name = "whatever", age = 12),
                Student(name = "whatever", age = 12)
            )
        )

        YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .build()
            .validate(toValidate)
    }

    companion object {
        private val yamlInputStream get() = TestTagProcessorEQ::class.java.getResourceAsStream("/eq.v.yaml")
    }
}
