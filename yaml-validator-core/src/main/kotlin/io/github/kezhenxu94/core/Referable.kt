package io.github.kezhenxu94.core

/**
 * [Referable] indicates that the actual value(s) that a validator validates can be referenced later,
 * and be retrieved by [reference].
 */
interface Referable<T> {
  val reference: T?

  /**
   * Reset the [reference] to null.
   */
  fun reset()
}
