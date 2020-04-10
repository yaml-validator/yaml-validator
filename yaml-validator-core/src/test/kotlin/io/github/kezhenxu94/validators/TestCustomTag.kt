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
import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.core.Context
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.yaml.snakeyaml.constructor.AbstractConstruct
import org.yaml.snakeyaml.nodes.Node

internal class TestCustomTag {
    internal companion object {
        // tag::customTag[]
        @TagProcessor(tags = ["!fail"], construct = AlwaysFailConstruct::class) // <1>
        class AlwaysFailTag(override val context: Context) : Validatable { // <2>
            override fun validate(any: Any?) {
                throw ValidateException("always fail") // <3>
            }
        }
        // end::customTag[]

        // tag::customConstruct[]
        class AlwaysFailConstruct : AbstractConstruct() { // <1>
            override fun construct(node: Node): Any {
                return AlwaysFailTag(Context(node)) // <2>
            }
        }
        // end::customConstruct[]
    }

    // tag::test[]
    @Test
    fun `should pass when custom tag`() {
        assertThrows<ValidateException> {
            YamlValidator.from(
                """
                test: !fail # <1>
                """.trimIndent()
            )
                .register(AlwaysFailTag::class) // <2>
                .build()
                .validate(mapOf("test" to "abc"))
        }
    }
    // end::test[]

    @Test
    fun `should pass when custom tag 1`() {
        assertThrows<ValidateException> {
            YamlValidator.from(
                """
                test: !fail
                """.trimIndent()
            )
                .register(AlwaysFailTag::class.java)
                .build()
                .validate(mapOf("test" to "abc"))
        }
    }

    @Test
    fun `should pass when custom tag from package`() {
        assertThrows<ValidateException> {
            YamlValidator.from(
                """
                test: !fail
                """.trimIndent()
            )
                .register(AlwaysFailTag::class.java.`package`.name)
                .build()
                .validate(mapOf("test" to "abc"))
        }
    }
}
