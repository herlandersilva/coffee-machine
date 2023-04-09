package machine

import nl.hiddewieringa.money.ofCurrency
import java.util.*
import javax.money.CurrencyUnit
import javax.money.MonetaryAmount
import javax.money.MonetaryContext

class CoffeeMachine {
    private var cups: Int = 0
    private var money: MonetaryAmount = (0).ofCurrency()<MonetaryContext>("EUR")
    private val waterSupplier: Volume = Volume(Ingredient.WATER, 0U, Unit.ML)
    private val milkSupplier: Volume = Volume(Ingredient.MILK, 0U, Unit.ML)
    private val coffeeBeansSupplier: Volume = Volume(Ingredient.COFFEE_BEANS, 0U, Unit.GM)

    fun `Starting to make a coffee`() = println("Starting to make a coffee")
    fun `Grinding coffee beans`() = println("Grinding coffee beans")
    fun `Boiling water`() = println("Boiling water")
    fun `Mixing boiled water with crushed coffee beans`() = println("Mixing boiled water with crushed coffee beans")
    fun `Pouring coffee into the cup`() = println("Pouring coffee into the cup")
    fun `Pouring some milk into the cup`() = println("Pouring some milk into the cup")
    fun `Coffee is ready!`() = println("Coffee is ready!")
    fun `How many ingredients to make x cups of coffee`() = this.howManyIngredientsNeedToMakeCoffeeXCups()
    fun `How many supplier the machine has`() = this.howManySupplierTheMachineHas()
    fun `Buy, fill and take`() = this.buyFillAndTake()


    override fun toString(): String {
        return "For %d cups of coffee you will need:".format(cups)
    }

    private fun howManyIngredientsNeedToMakeCoffeeXCups() {
        this.cups = Util.ask("Write how many cups of coffee you will need:").toInt()
        println(this.toString())
        val stdCoffeeCup = StandardCoffeeCup()
        val customWater: Volume = Volume(
            stdCoffeeCup.waterVolume.ingredient,
            stdCoffeeCup.waterVolume.quantity * cups.toUInt(),
            stdCoffeeCup.waterVolume.unit
        )
        val customMilk: Volume = Volume(
            stdCoffeeCup.milkVolume.ingredient,
            stdCoffeeCup.milkVolume.quantity * cups.toUInt(),
            stdCoffeeCup.milkVolume.unit
        )
        val customCoffeeBeans: Volume = Volume(
            stdCoffeeCup.coffeeVolume.ingredient,
            stdCoffeeCup.coffeeVolume.quantity * cups.toUInt(),
            stdCoffeeCup.coffeeVolume.unit
        )
        val customCoffeeCup = CustomCoffeeCup(customWater, customMilk, customCoffeeBeans)
        println(customCoffeeCup.waterVolume)
        println(customCoffeeCup.milkVolume)
        println(customCoffeeCup.coffeeVolume)
    }

    private fun howManySupplierTheMachineHas() {
        val waterSupplier = Volume(
            Ingredient.WATER,
            Util.ask("Write how many %s of %s the coffee machine has:".format(Unit.ML.desc, Ingredient.WATER.desc)).toUInt(),
            Unit.ML
        )
        val milkSupplier = Volume(
            Ingredient.MILK,
            Util.ask("Write how many %s of %s the coffee machine has:".format(Unit.ML.desc, Ingredient.MILK.desc)).toUInt(),
            Unit.ML
        )
        val coffeeBeansSupplier = Volume(
            Ingredient.COFFEE_BEANS,
            Util.ask("Write how many %s of %s the coffee machine has:".format(Unit.GM.desc, Ingredient.COFFEE_BEANS.desc)).toUInt(),
            Unit.GM
        )
        this.cups = Util.ask("Write how many cups of coffee you will need:").toInt()
        val nimOfCoffeePossible = StandardCoffeeCup().haveManySupplierToMakeCupOfCoffee(waterSupplier, milkSupplier, coffeeBeansSupplier)
        when {
            this.cups == nimOfCoffeePossible -> "Yes, I can make that amount of coffee"
            this.cups > nimOfCoffeePossible -> "No, I can make only %d cups of coffee".format(nimOfCoffeePossible)
            else -> "Yes, I can make that amount of coffee (and even %d more than that)".format(nimOfCoffeePossible - this.cups)
        }.also(::println)
    }

    private fun buyFillAndTake() {

    }
}

abstract class CoffeeCup {
    abstract val waterVolume: Volume
    abstract val milkVolume: Volume
    abstract val coffeeVolume: Volume
}

class CustomCoffeeCup(
    val _waterVolume: Volume,
    val _milkVolume: Volume,
    val _coffeeVolume: Volume
) : CoffeeCup() {
    override val waterVolume = _waterVolume
    override val milkVolume = _milkVolume
    override val coffeeVolume = _coffeeVolume
}

class StandardCoffeeCup : CoffeeCup() {
    override val waterVolume = Volume(Ingredient.WATER, 200U, Unit.ML)
    override val milkVolume = Volume(Ingredient.MILK, 50U, Unit.ML)
    override val coffeeVolume = Volume(Ingredient.COFFEE_BEANS, 15U, Unit.GM)

    fun haveManySupplierToMakeCupOfCoffee(waterSupplier: Volume, milkSupplier: Volume, coffeeBeansSupplier: Volume): Int {
        return listOf(
            waterSupplier.quantity / waterVolume.quantity,
            milkSupplier.quantity / milkVolume.quantity,
            coffeeBeansSupplier.quantity / coffeeVolume.quantity
        ).minOf { it.toInt() }
    }
}