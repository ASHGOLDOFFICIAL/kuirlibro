package org.aulune.kuirlibro.domain

import java.math.BigDecimal

/**
 * Why constructing a [Price] was rejected.
 */
sealed interface PriceError {

    /**
     * [raw] is not positive.
     *
     * @property raw the rejected value.
     */
    data class NonPositivePrice(val raw: BigDecimal) : PriceError
}
