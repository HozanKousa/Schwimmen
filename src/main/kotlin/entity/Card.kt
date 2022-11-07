package entity

/**
 * Data class for the single typ of game elements that the game "Schimmen" knows: cards.
 *
 * It is characterized by a [CardSuit] and a [CardValue]
 */

data class Card(val type : CardSuit, val value : CardValue) {


    /**
     * returns the Card int value of each card ->
     * K,Q,J(10 points)
     * A (11 points)
     * 7,8,9,10 (7,8,9,10 points)
     */
    fun cardValue() : Int{
        return try {
            value.toString().toInt()
        } catch (e: NumberFormatException) {
            if (value.toString() == "A") 11
            else 10
        }
    }

    override fun toString() : String =
        "${type.toString()}${value.toString()}"
}