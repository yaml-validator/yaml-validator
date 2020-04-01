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

package com.github.kezhenxu94

import com.github.kezhenxu94.exceptions.ValidateException
import com.github.kezhenxu94.validators.nn.NotNullValidator
import org.yaml.snakeyaml.Yaml
import java.io.InputStream

class YamlValidator private constructor(private val builder: Builder) {
  private val validator = Yaml(RootConstructor).loadAs(builder.inputStream, Map::class.java)

  fun validate(toValidate: Any?) {
    return when {
      validator is Validatable -> validator.validate(toValidate)
      toValidate is String     -> traverse(validator, Loader.loadAs(toValidate, Map::class.java))
      else                     -> traverse(validator, Loader.loadAs(Dumper.dump(toValidate), Map::class.java))
    }
  }

  private fun traverse(validator: Any, toValidate: Any?) {
    when (validator) {
      is Validatable -> {
        if (!builder.ignoreMissing || validator is NotNullValidator || toValidate != null) {
          validator.validate(toValidate)
        }
      }

      is Map<*, *>   -> {
        if (toValidate !is Map<*, *>) {
          throw ValidateException()
        }
        validator.forEach { (k, v) ->
          traverse(v!!, toValidate[k])
        }
      }

      is List<*>     -> {
        if (toValidate !is List<*>) {
          throw ValidateException()
        }
        validator.forEachIndexed { index, v ->
          traverse(v!!, toValidate[index])
        }
      }
    }
  }

  companion object {
    class Builder(
        internal val inputStream: InputStream,
        internal var ignoreMissing: Boolean = false
    ) {

      fun ignoreMissing() = this.also { ignoreMissing = true }

      fun build() = YamlValidator(this)
    }

    fun from(inputStream: InputStream) = Builder(inputStream)

    fun from(yaml: String) = Builder(yaml.byteInputStream())
  }
}
