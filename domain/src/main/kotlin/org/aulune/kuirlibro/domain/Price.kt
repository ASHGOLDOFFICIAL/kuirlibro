package org.aulune.kuirlibro.domain

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

/**
 * A positive monetary amount in a [currency], rounded half-up.
 *
 * @property value must be positive.
 * @property currency this price's currency.
 */
@ConsistentCopyVisibility
data class Price private constructor(val value: BigDecimal, val currency: Currency) {

    /** Holds [Price]'s smart constructor. */
    companion object {
        private const val SCALE = 2

        private fun scaled(raw: BigDecimal): BigDecimal = raw.setScale(SCALE, RoundingMode.HALF_UP)

        /** Constructs a [Price] from [raw] in [currency], failing if [raw] is not positive. */
        operator fun invoke(raw: BigDecimal, currency: Currency): Either<PriceError, Price> = either {
            val value = scaled(raw)
            ensure(value > BigDecimal.ZERO) { PriceError.NonPositivePrice(raw) }
            Price(value, currency)
        }
    }
}
