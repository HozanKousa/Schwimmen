package entity

/**
 * Entity to represent a player in the game "Schwimmen".
 *
 * @param playerName represents the player name
 */
class Player(val playerName : String) {

    /**
     *  passed: represents the player's pass status.
     *  knocked: represents the player's knock status.
     *  score: represents the player's score points.
     *  hand: is an Array of three Cards, that the player can play with.
     */
    var passed : Boolean = false
    var knocked : Boolean = false
    var score : Double = 0.0
    var hand : ArrayDeque<Card> = ArrayDeque<Card>(3)


    override fun toString() : String{
        return "$playerName has a list of Cards: ${hand.toString()}"
    }


}