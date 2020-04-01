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

import io.github.kezhenxu94.annotations.Validator
import org.reflections.Reflections
import org.yaml.snakeyaml.constructor.Construct
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag

internal object RootConstructor : Constructor() {
  init {
    val validatorClasses = Reflections(Package::class.java.`package`.name).getTypesAnnotatedWith(Validator::class.java)

    for (klass in validatorClasses) {
      val validator = klass.getAnnotation(Validator::class.java)

      for (tag in validator.tags) {
        yamlConstructors[Tag(tag)] = validator.construct.java.newInstance()
      }

      for (prefix in validator.prefixes) {
        yamlMultiConstructors[prefix] = validator.construct.java.newInstance()
      }
    }
  }

  val constructs: Map<Tag, Construct> get() = yamlConstructors
}