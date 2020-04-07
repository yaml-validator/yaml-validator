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

package io.github.kezhenxu94.validators.composite.unorder

import io.github.kezhenxu94.YamlValidator
import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.core.Context
import io.github.kezhenxu94.core.Referable
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.representers.MapRepresenter
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Tag

@TagProcessor(tags = ["!unordered", "!uo"], construct = UnorderedConstruct::class)
internal class UnorderedValidator(override val context: Context) : Validatable, Referable<String> {
    override var reference: String? = null

    private val nodes = context.root?.constructs!![Tag.SEQ]?.construct(context.node)

    override fun validate(any: Any?) {
        if (nodes !is List<*>) {
            throw ValidateException(context, expected = "list", actual = nodes?.javaClass)
        }

        val candidates = when (any) {
            is List<*> -> any
            else -> Yaml().loadAs(Yaml(MapRepresenter()).dump(any), List::class.java)
        }

        if (nodes.size != candidates.size) {
            throw ValidateException(
                context, expected = "size ${nodes.size}", actual = "size ${candidates.size}"
            )
        }

        val candidateSatisfies = { validator: Any? ->
            candidates.any { candidate ->
                try {
                    if (validator == candidate) {
                        return@any true
                    }
                    if (validator is Validatable) {
                        validator.validate(candidate)
                    }
                    YamlValidator.from(validator = validator!!).build().validate(candidate)
                    return@any true
                } catch (_: ValidateException) {
                    return@any false
                }
            }
        }

        if (!nodes.all { candidateSatisfies(it) }) {
            throw ValidateException()
        }
    }

    override fun reset() {
        reference = null
    }
}
