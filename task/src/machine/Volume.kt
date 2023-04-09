package machine

data class Volume(val ingredient: Ingredient, val quantity: UInt, val unit: machine.Unit) {
    override fun toString(): String {
        return "%d %s of %s".format(quantity.toInt(), unit, ingredient.desc)
    }
}