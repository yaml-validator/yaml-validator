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

internal class TestYamlValidator {
    // tag::simple[]
    @Test
    fun `should pass when no tag and raw strings match`() {
        YamlValidator.from(
            """
            test: abc
            """.trimIndent()
        )
            .build()
            .validate(mapOf("test" to "abc"))
    }
    // end::simple[]

    @Test
    fun `should pass when candidate is stream`() {
        YamlValidator.from(
            """
            test: 123.5
            """.trimIndent()
        )
            .build()
            .validate("test: 123.5".byteInputStream())
    }

    @Test
    fun `should pass when no tag and raw number match`() {
        YamlValidator.from(
            """
            test: 123.5
            """.trimIndent()
        )
            .build()
            .validate(mapOf("test" to "123.5"))
    }

    @Test
    fun `should fail when validator null but candidate not null`() {
        assertThrows<ValidateException> {
            YamlValidator.from(
                """
                test: ~
                """.trimIndent()
            )
                .build()
                .validate(mapOf("test" to "123.5"))
        }
    }

    @Test
    fun `should fail when no tag and raw string mismatch`() {
        assertThrows<ValidateException> {
            YamlValidator.from(
                """
                test: abc
                """.trimIndent()
            )
                .build()
                .validate(mapOf("test" to "abcd"))
        }
    }
}
