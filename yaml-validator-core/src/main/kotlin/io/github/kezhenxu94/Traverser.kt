package io.github.kezhenxu94

import io.github.kezhenxu94.core.Referable
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.utils.Dumper
import io.github.kezhenxu94.utils.Loader

internal class Traverser(private val builder: YamlValidator.Companion.Builder) {

    fun traverse(validator: Any, candidate: Any?) {
        when (validator) {
            is Validatable -> validate0(validator, candidate)

            is Map<*, *> -> validator.forEach { (k, v) ->
                val asMap = { any: Any? -> Loader().loadAs(Dumper().dump(any), Map::class.java) }
                val candidateVal = ((candidate as? Map<*, *>) ?: asMap(candidate))[k]
                if (v != null) {
                    traverse(v, candidateVal)
                } else if (candidateVal != null) {
                    throw ValidateException()
                }
            }

            is List<*> -> validator.forEachIndexed { index, v ->
                traverse(v!!, (candidate as List<*>)[index])
            }

            else -> validateRaw(validator, candidate)
        }
    }

    private fun validateRaw(validator: Any, toValidate: Any?) {
        if (validator.toString() != toValidate?.toString()) {
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
}
