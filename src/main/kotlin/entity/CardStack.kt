package entity

import java.util.*
import kotlin.collections.ArrayDeque

/**
 * Data structure that holds [Card] objects and provides stack-like
 * access to them (with e.g. [putOnTop], [draw]).
 */
class CardStack {
    /**
     * The actual backing data structure. As there is no dedicated stack implementation
     * in Kotlin, a "double-ended queue" (Deque) is used.
     */
    val cards: ArrayDeque<Card> = ArrayDeque(32)

    /**
     * the amount of cards in this stack
     */
    val size : Int get() = cards.size

    /**
     * Returns `true` if the stack is empty, `false` otherwise.
     */
    val empty : Boolean get() = cards.isEmpty()

    /**
     * Shuffles the cards in this stack
     */
    fun shuffle() {
        cards.shuffle()
    }

    /**
     * @param [random] can be provided to ensure deterministic behavior of [shuffle]
     */
    fun shuffle(random : Random) {
        cards.shuffle(random)
    }

    /**
     * Draws [amount] cards from this stack.
     *
     * @param amount the number of cards to draw; defaults to 1 if omitted.
     *
     * @throws IllegalArgumentException if not enough cards on stack to draw the desired amount.
     */
    fun draw(amount : Int =1) : ArrayDeque<Card>{
        require(amount in 1..size){ "Can't draw $amount from $cards" }
        val drawnCards : ArrayDeque<Card> = ArrayDeque<Card>()
        var count = amount
        while(count>0){
            drawnCards.add(cards.first())
            cards.removeFirst()
            count--
        }
        return drawnCards
    }

    /**
     * provides a view of the full stack contents without changing it. Use [draw]
     * for actually drawing cards from this stack.
     */
    fun peekAll(): List<Card> = cards.toList()

    /**
     * puts a given list of cards on top of this card stack, so that
     * the last element of the passed parameter [cards] will be on top of
     * the stack afterwards.
     */
    fun putOnTop(cards: List<Card>) {
        cards.forEach(this.cards::addFirst)
    }

    /**
     * puts the given card on top of this card stack
     */
    fun putOnTop(card: Card) {
        cards.addFirst(card)
    }
}