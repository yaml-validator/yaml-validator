/**
 * Copyright 2020 yaml-validator
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
import org.yaml.snakeyaml.constructor.ConstructorException

internal class TestTagProcessorNN {
    @Test
    internal fun `should pass when top level object is validatable`() {
        val toValidate = Yaml().loadAs(
            "name: abc",
            Map::class.java
        )
        YamlValidator.from("name: !nn")
            .ignoreMissing()
            .build()
            .validate(toValidate)
    }

    @Test
    internal fun `should pass when top level object is string`() {
        YamlValidator.from("name: !nn")
            .ignoreMissing()
            .build()
            .validate("name: abc")
    }

    @Test
    internal fun `should pass when top level is list`() {
        YamlValidator.from(
            """
            - name: !nn
              age: &age !gt 12
            """.trimIndent()
        )
            .ignoreMissing()
            .build()
            .validate(
                """
                - name: Nakajima Miyuki
                  age: 13
                """.trimIndent()
            )
    }

    // tag::demo[]
    @Test
    internal fun `should pass when nn`() {
        val toValidate = Yaml().loadAs(
            """
            students:
              - name: abc # <1>
                age: 23 # <2>
              - name: abcd
                age: 23 # <3>
            """.trimIndent(),
            Map::class.java
        )
        YamlValidator.from(yamlInputStream)
            .ignoreMissing() // <4>
            .build()
            .validate(toValidate)
    }
    // end::demo[]

    @Test
    internal fun `should fail when null`() {
        assertThrows<ValidateException> {
            val toValidate = Yaml().loadAs(
                """
                students:
                  - name: ~
                    age: 23
                  - name: ~
                    age: "23"
                """.trimIndent(),
                Map::class.java
            )
            YamlValidator.from(yamlInputStream)
                .build()
                .validate(toValidate)
        }
    }

    @Test
    internal fun `should fail when not equal to anchor`() {
        assertThrows<ValidateException> {
            val toValidate = Yaml().loadAs(
                """
                students:
                  - name: whatever
                    age: 23
                  - name: whatever
                    age: 24
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
    internal fun `should fail when type mismatch`() {
        assertThrows<Exception> {
            val toValidate = Yaml().loadAs(
                """
                student:
                  name: whatever
                  age: 23
                """.trimIndent(),
                Map::class.java
            )
            YamlValidator.from(yamlInputStream)
                .ignoreMissing()
                .build()
                .validate(toValidate)
        }

        assertThrows<Exception> {
            val toValidate = Yaml().loadAs(
                """
                student: ~
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
    internal fun `should fail when type mismatch 2`() {
        assertThrows<ConstructorException> {
            val toValidate = Yaml().loadAs(
                """
                students:
                  - name: abc
                    age: 23
                  - 1
                """.trimIndent(),
                Map::class.java
            )
            YamlValidator.from(yamlInputStream)
                .ignoreMissing()
                .build()
                .validate(toValidate)
        }
    }

    private companion object {
        private val yamlInputStream get() = TestTagProcessorNN::class.java.getResourceAsStream("/nn.v.yaml")
    }
}
