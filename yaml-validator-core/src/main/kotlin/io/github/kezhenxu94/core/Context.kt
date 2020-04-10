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

package io.github.kezhenxu94.core

import io.github.kezhenxu94.RootConstructor
import org.yaml.snakeyaml.nodes.Node

/**
 * Holds the context of a validation process.
 */
class Context(
    /**
     * The [Node] that this validation belongs to.
     */
    val node: Node,
    /**
     * The root `Constructor` in the validation context.
     */
    val root: RootConstructor? = null
)
