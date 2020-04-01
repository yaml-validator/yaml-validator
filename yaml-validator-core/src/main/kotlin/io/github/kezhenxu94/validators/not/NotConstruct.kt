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

package io.github.kezhenxu94.validators.not

import io.github.kezhenxu94.RootConstructor
import io.github.kezhenxu94.Validatable
import org.yaml.snakeyaml.constructor.AbstractConstruct
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.nodes.Tag

internal class NotConstruct : AbstractConstruct() {
  override fun construct(node: Node): Any {
    node.tag = Tag(node.tag.value.replace("!not.", "!"))
    val construct = RootConstructor.constructs[node.tag] ?: throw IllegalStateException()
    return NotValidator(construct.construct(node) as Validatable)
  }
}
