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

internal class TestTagProcessorJoin {
    @Test
    internal fun `should pass when joining`() {
        val toValidate = Yaml().loadAs(
            """
            graph:
              - source:
                  id: 1
              - target:
                  id: 2
            relation: "1_2"
            """.trimIndent(),
            Map::class.java
        )
        YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .build()
            .validate(toValidate)
    }

    @Test
    internal fun `should fail when joining not eq`() {
        assertThrows<ValidateException> {
            val toValidate = Yaml().loadAs(
                """
                graph:
                  - source:
                      id: 1
                  - target:
                      id: 2
                relation: "1.0_2.0"
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
        private val yamlInputStream get() = TestTagProcessorJoin::class.java.getResourceAsStream("/join.v.yaml")
    }
}
