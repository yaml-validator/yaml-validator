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

import io.github.kezhenxu94.core.Referable
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/**
 * A helper class to make it easy to construct a real [Validatable] instance.
 */
class YamlValidator private constructor(private val builder: Builder) {
  private val validator = when {
    builder.validator != null   -> builder.validator
    builder.inputStream != null -> Yaml(RootConstructor()).loadAs(builder.inputStream, Map::class.java)
    else                        -> throw IllegalStateException()
  }

  /**
   * @see [Validatable.validate]
   */
  fun validate(toValidate: Any?) {
    return when (toValidate) {
      is InputStream -> traverse(validator, Loader().loadAs(toValidate, Map::class.java))
      is String      -> traverse(validator, Loader().loadAs(toValidate, Map::class.java))
      is Map<*, *>,
      is List<*>     -> traverse(validator, toValidate)
      else           -> traverse(validator, Loader().loadAs(Dumper().dump(toValidate), Map::class.java))
    }
  }

  private fun traverse(validator: Any, toValidate: Any?) {
    when (validator) {
      is Validatable -> validate0(validator, toValidate)

      is Map<*, *>   -> validator.forEach { (k, v) ->
        traverse(v!!, ((toValidate as? Map<*, *>) ?: Loader().loadAs(Dumper().dump(toValidate), Map::class.java))[k])
      }

      is List<*>     -> validator.forEachIndexed { index, v ->
        traverse(v!!, (toValidate as List<*>)[index])
      }

      is String      -> validateString(validator, toValidate)
    }
  }

  private fun validateString(validator: Any, toValidate: Any?) {
    if (validator != toValidate?.toString()) {
      val message = """
      raw validation failed
      Expect: $validator
      Actual: $toValidate
      """.trimIndent()
      throw ValidateException(message)
    }
  }

  private fun validate0(validator: Validatable, toValidate: Any?) {
    if (builder.disableReference) {
      (validator as? Referable<*>)?.reset()
    }
    if (!builder.ignoreMissing || toValidate != null) {
      validator.validate(toValidate)
    }
  }

  companion object {
    class Builder(
        internal val inputStream: InputStream? = null,
        internal val validator: Any? = null,
        internal var ignoreMissing: Boolean = false,
        internal var disableReference: Boolean = false
    ) {

      /**
       * Ignore the missing fields when validating.
       */
      fun ignoreMissing() = this.also { ignoreMissing = true }

      /**
       * Ignore the referenced anchor when validating, just validates as if it was the first time when the validator is
       * used.
       */
      fun disableReference() = this.also { disableReference = true }

      /**
       * Build a real [Validatable] instance from this [Builder].
       */
      fun build() = YamlValidator(this)
    }

    /**
     * Create a [Builder] from the YAML [InputStream].
     */
    fun from(inputStream: InputStream) = Builder(inputStream)

    /**
     * Create a [Builder] from the [yaml] text string.
     */
    fun from(yaml: String) = Builder(yaml.byteInputStream())

    /**
     * Create a [Builder] from the [validator].
     */
    fun from(validator: Any) = Builder(validator = validator)
  }
}
