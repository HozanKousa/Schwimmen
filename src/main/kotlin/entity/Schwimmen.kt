package entity


/**
 * Entity class that represents a game state of "Schwimmen". As hand card stack information for each
 * player is stored in the [Player] entity, this class is a wrapper for an array of players objects.
 * and an object from [CardStack] that represents the stack of 32 Cards.
 *
 */
class Schwimmen(val players : ArrayDeque<Player>, val cards : CardStack) {

    /**
     * @param table contains 3 cards, that will be shown on table in front of all players
     */
    var table : ArrayDeque<Card> = ArrayDeque<Card>(3)

    /**
     * @param activePlayer represents the active player.
     */
    lateinit var activePlayer : Player

    /**
     * @param discardStack is an Array of discarded cards.
     */
    val discardStack : CardStack = CardStack()
}