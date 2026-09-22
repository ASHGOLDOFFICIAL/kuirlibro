package org.aulune.kuirlibro.domain.ingredient

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import org.aulune.kuirlibro.domain.ImageRef
import org.aulune.kuirlibro.domain.MeasurementKind
import org.aulune.kuirlibro.domain.NutritionFacts
import org.aulune.kuirlibro.domain.Price

/**
 * Pure state transitions for an [Ingredient].
 */
object IngredientRules {

    /** A new, active ingredient with [id], [name], [measurementKind], [nutritionPerUnit] and
     * [pricePerUnit]. */
    fun create(
        id: IngredientId,
        name: IngredientName,
        measurementKind: MeasurementKind,
        nutritionPerUnit: NutritionFacts,
        pricePerUnit: Price?,
    ): Either<IngredientError, Ingredient> = either {
        Ingredient(
            id = id,
            name = name,
            measurementKind = measurementKind,
            nutritionPerUnit = nutritionPerUnit,
            pricePerUnit = pricePerUnit,
            image = null,
            status = IngredientStatus.ACTIVE,
        )
    }

    /** [current] with its name, nutritional content, price and image replaced by [name],
     * [nutritionPerUnit], [pricePerUnit] and [image]. */
    fun update(
        current: Ingredient,
        name: IngredientName,
        nutritionPerUnit: NutritionFacts,
        pricePerUnit: Price?,
        image: ImageRef?,
    ): Either<IngredientError, Ingredient> = either {
        ensureActive(current)
        current.copy(name = name, nutritionPerUnit = nutritionPerUnit, pricePerUnit = pricePerUnit, image = image)
    }

    /** [current] soft-deleted. */
    fun delete(current: Ingredient): Either<IngredientError, Ingredient> = either {
        ensureActive(current)
        current.copy(status = IngredientStatus.DELETED)
    }

    /** [current] restored from a soft delete. */
    fun undelete(current: Ingredient): Either<IngredientError, Ingredient> = either {
        ensureDeleted(current)
        current.copy(status = IngredientStatus.ACTIVE)
    }

    private fun Raise<IngredientError>.ensureActive(current: Ingredient) {
        ensure(current.status == IngredientStatus.ACTIVE) { IngredientError.AlreadyDeleted }
    }

    private fun Raise<IngredientError>.ensureDeleted(current: Ingredient) {
        ensure(current.status == IngredientStatus.DELETED) { IngredientError.NotDeleted }
    }
}
