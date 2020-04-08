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

package io.github.kezhenxu94.validators.composite.anyof

import io.github.kezhenxu94.RootConstructor
import io.github.kezhenxu94.YamlValidator
import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.core.Context
import io.github.kezhenxu94.core.Referable
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import org.yaml.snakeyaml.nodes.Tag

@TagProcessor(tags = ["!anyOf"], construct = AnyOfConstruct::class)
internal class AnyOfValidator(override val context: Context) : Validatable, Referable<Any> {
    override var reference: Any? = null

    private val validator: Any

    init {
        val node = context.node
        validator = (RootConstructor().constructs[Tag.MAP] ?: error("should never happen")).construct(node)
    }

    override fun validate(any: Any?) {
        val yamlValidator = YamlValidator.from(validator = validator).disableReference().build()

        (any as List<*>).firstOrNull {
            try {
                yamlValidator.validate(it)
                reference = it
                true
            } catch (_: ValidateException) {
                false
            }
        } ?: throw ValidateException(context)
    }
}
