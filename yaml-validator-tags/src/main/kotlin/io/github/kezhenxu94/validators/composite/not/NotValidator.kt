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

package io.github.kezhenxu94.validators.composite.not

import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.core.Context
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.validators.composite.not.NotValidator.Companion.PREFIX
import org.yaml.snakeyaml.nodes.Tag

@TagProcessor(prefixes = [PREFIX], construct = NotConstruct::class)
internal class NotValidator(override val context: Context) : Validatable {
    companion object {
        internal const val PREFIX = "!not."
    }

    private val validatable: Validatable

    init {
        val node = context.node
        node.tag = Tag(node.tag.value.replace(PREFIX, "!"))
        validatable =
            context.root?.constructs!![node.tag]?.construct(node) as? Validatable ?: throw IllegalStateException()
    }

    override fun validate(any: Any?) {
        try {
            validatable.validate(any)
        } catch (_: ValidateException) {
            return
        }
        throw ValidateException()
    }
}
