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

package io.github.kezhenxu94

import io.github.kezhenxu94.core.Validatable
import java.io.InputStream
import org.yaml.snakeyaml.Yaml

/**
 * A helper class to make it easy to construct a real [Validatable] instance.
 */
class YamlValidator private constructor(builder: Builder) {
    private val traverser = Traverser(builder)
    private val validator = builder.validator ?: Yaml(RootConstructor()).load(builder.inputStream)

    /**
     * See [Validatable.validate]
     */
    fun validate(candidate: Any?) {
        return when (candidate) {
            is String -> traverser.traverse(validator, Loader().load(candidate))
            is InputStream -> traverser.traverse(validator, Loader().load(candidate))
            is Map<*, *>, is List<*> -> traverser.traverse(validator, candidate)
            else -> traverser.traverse(validator, Loader().load(Dumper().dump(candidate)))
        }
    }

    companion object {
        /**
         * Builder pattern to help build a `YamlValidator`.
         */
        class Builder(
            internal val inputStream: InputStream? = null,
            internal val validator: Any? = null,
            internal var ignoreMissing: Boolean = false,
            internal var disableReference: Boolean = false
        ) {

            /**
             * Ignore the missing fields when validating.
             */
            fun ignoreMissing() = this.also { ignoreMissing = true }

            /**
             * Ignore the referenced anchor when validating, just validates as if it was the first time when the
             * validator is used.
             */
            fun disableReference() = this.also { disableReference = true }

            /**
             * Build a real [Validatable] instance from this [Builder].
             */
            fun build() = YamlValidator(this)
        }

        /**
         * Create a [Builder] from the YAML [InputStream].
         */
        fun from(inputStream: InputStream) = Builder(inputStream)

        /**
         * Create a [Builder] from the [yaml] text string.
         */
        fun from(yaml: String) = Builder(yaml.byteInputStream())

        /**
         * Create a [Builder] from the [validator].
         */
        fun from(validator: Any) = Builder(validator = validator)
    }
}
