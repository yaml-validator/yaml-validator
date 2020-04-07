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

internal class TestTagProcessorUO {
    @Test
    internal fun `should pass when unordered`() {
        val toValidate = Yaml().loadAs(
            """
            songs:
              - name: Singles Bar
                artist: Nakajima Miyuki
              - name: 永遠の嘘をついてくれ
                artist: Nakajima Miyuki
              - name: この世に二人だけ
                artist: Nakajima Miyuki
            """.trimIndent(),
            Map::class.java
        )
        YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .build()
            .validate(toValidate)
    }

    @Test
    internal fun `should pass when nested`() {
        val toValidate = Yaml().loadAs(
            """
            songs:
              - name: 永遠の嘘をついてくれ
                artist: Nakajima Miyuki
              - name: Singles Bar
                artist: Nakajima Miyuki
              - name: この世に二人だけ
                artist: Nakajima Miyuki
            """.trimIndent(),
            Map::class.java
        )
        YamlValidator.from(yamlInputStream)
            .ignoreMissing()
            .build()
            .validate(toValidate)
    }

    @Test
    internal fun `should fail when any nil`() {
        assertThrows<ValidateException> {
            val toValidate = Yaml().loadAs(
                """
                songs:
                  - name: 永遠の嘘をついてくれ
                    artist: Nakajima Miyuki
                  - name: Singles Bar - Nakajima Miyuki
                    artist: Nakajima Miyuki
                  - name: この世に二人だけ
                    artist: Nakajima Miyuki
                """.trimIndent(),
                Map::class.java
            )
            YamlValidator.from(yamlInputStream)
                .ignoreMissing()
                .build()
                .validate(toValidate)
        }
    }

    companion object {
        private val yamlInputStream get() =
            TestTagProcessorUO::class.java.getResourceAsStream("/unordered.v.yaml")
    }
}
