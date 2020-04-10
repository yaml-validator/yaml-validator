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
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.representers.MapRepresenter
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Tag

@TagProcessor(tags = ["!unordered", "!uo"], construct = UnorderedConstruct::class)
internal class UnorderedValidator(override val context: Context) : Validatable {

    private val nodes = context.root?.constructs!![Tag.SEQ]?.construct(context.node) as List<*>

    override fun validate(candidate: Any?) {
        val candidates = candidate as? List<*> ?: Yaml().loadAs(
            Yaml(MapRepresenter()).dump(candidate), List::class.java
        )

        val candidateSatisfies = { validator: Any? ->
            candidates.any { candidate ->
                if (validator == candidate)
                    true
                else try {
                    if (validator is Validatable) {
                        validator.validate(candidate)
                    } else {
                        YamlValidator.from(validator = validator!!).build().validate(candidate)
                    }
                    true
                } catch (_: ValidateException) {
                    false
                }
            }
        }

        if (!nodes.all { candidateSatisfies(it) }) {
            throw ValidateException(context)
        }
    }
}
