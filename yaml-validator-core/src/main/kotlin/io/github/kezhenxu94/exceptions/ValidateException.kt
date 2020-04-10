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

package io.github.kezhenxu94.exceptions

import io.github.kezhenxu94.core.Context

/**
 * Exception indicates the validations are failed.
 */
class ValidateException(message: String? = null) : Exception(message) {
    constructor(context: Context, expected: Any? = null, actual: Any? = null) : this(
        """
        ${context.node.tag?.value} validation failed
        at line: ${context.node.startMark.line + 1}, column: ${context.node.startMark.column + 1}
        expected: ${context.node.tag?.value} $expected
        actual: $actual
        """.trimIndent()
    )
}
