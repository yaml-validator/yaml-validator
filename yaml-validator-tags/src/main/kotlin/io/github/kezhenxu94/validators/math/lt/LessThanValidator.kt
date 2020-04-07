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

package io.github.kezhenxu94.validators.math.lt

import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.core.Context
import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.validators.math.MathValidator
import io.github.kezhenxu94.validators.math.lt.LessThanValidator.Companion.TAG
import org.yaml.snakeyaml.nodes.ScalarNode

@TagProcessor(tags = [TAG], construct = LessThanConstruct::class)
internal class LessThanValidator(override val context: Context) :
    MathValidator((context.node as ScalarNode).value.toDouble()) {

    internal companion object {
        internal const val TAG = "!lt"
    }

    override val tag = TAG

    override fun validateAnchor(anchor: Number) {
        if (anchor.toDouble() >= expected.toDouble()) {
            throw ValidateException(context, expected, anchor)
        }
    }

    override fun validateAlias(alias: Number) {
        if (alias.toDouble() != reference.toString().toDouble()) {
            throw ValidateException(context, reference, alias)
        }
    }
}
