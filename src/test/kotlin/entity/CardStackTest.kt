package entity
import java.util.*
import kotlin.test.*

/**
 * Test cases for [CardStack]
 */
class CardStackTest {
    private val sevenOfClubs : Card = Card(CardSuit.CLUBS, CardValue.SEVEN)
    private val eightOfSpades : Card = Card(CardSuit.SPADES, CardValue.EIGHT)
    private val nineOfHearts : Card = Card(CardSuit.HEARTS, CardValue.NINE)
    private val tenOfDiamonds : Card = Card(CardSuit.DIAMONDS, CardValue.TEN)
    private val jackOfClubs : Card = Card(CardSuit.CLUBS, CardValue.JACK)

    /**
     * tests if both an array of cards and one card can be added to the card stack.
     */
    @Test
    fun testAddCards() {
        val cardStack = CardStack()
        val listOfCards : List<Card> = listOf(sevenOfClubs, eightOfSpades, nineOfHearts)
        cardStack.putOnTop(listOfCards)
        assertSame(cardStack.draw().first(), nineOfHearts)
        assertSame(cardStack.draw().first(), eightOfSpades)

        cardStack.putOnTop(jackOfClubs)
        cardStack.putOnTop(tenOfDiamonds)
        assertEquals(3, cardStack.size)
    }

    /**
     * tests if the shuffle works depending on the Seed 42.
     */
    @Test
    fun testShuffle(){
        val cardStack = CardStack()
        val listOfCards : List<Card> = listOf(sevenOfClubs, eightOfSpades, nineOfHearts)
        cardStack.putOnTop(listOfCards)

        cardStack.shuffle(random = Random(42))
        assertEquals(listOf(eightOfSpades, nineOfHearts, sevenOfClubs),cardStack.draw(3))
        assertEquals(0, cardStack.size)
    }

    /**
     * tests if an amount of cards can be drawn from the card stack.
     */
    @Test
    fun testDraw() {
        val cardStack = CardStack()
        val listOfCards : List<Card> = listOf(sevenOfClubs, eightOfSpades, nineOfHearts)
        cardStack.putOnTop(listOfCards)
        cardStack.putOnTop(tenOfDiamonds)
        cardStack.putOnTop(jackOfClubs)

        val drawnCards=cardStack.draw(2)
        assertEquals(drawnCards, listOf(jackOfClubs, tenOfDiamonds))
        assertEquals(3, cardStack.size)
    }
}