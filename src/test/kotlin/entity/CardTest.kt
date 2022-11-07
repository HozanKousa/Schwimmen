package entity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CardTest {
    private val sevenOfClubs : Card = Card(CardSuit.CLUBS, CardValue.SEVEN)
    private val eightOfSpades : Card = Card(CardSuit.SPADES, CardValue.EIGHT)
    private val nineOfHearts : Card = Card(CardSuit.HEARTS, CardValue.NINE)
    private val tenOfDiamonds : Card = Card(CardSuit.DIAMONDS, CardValue.TEN)
    private val jackOfClubs : Card = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfSpades : Card = Card(CardSuit.SPADES, CardValue.QUEEN)
    private val kingOfHearts : Card = Card(CardSuit.HEARTS, CardValue.KING)
    private val aceOfDiamonds : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)

    private val heartsChar = '\u2665'
    private val diamondsChar = '\u2666'
    private val spadesChar = '\u2660'
    private val clubsChar = '\u2663'

    @Test
    fun testToString(){
        assertEquals(clubsChar + "7", sevenOfClubs.toString())
        assertEquals(spadesChar + "8", eightOfSpades.toString())
        assertEquals(heartsChar + "9", nineOfHearts.toString())
        assertEquals(diamondsChar + "10", tenOfDiamonds.toString())
        assertEquals(clubsChar+ "J", jackOfClubs.toString())
        assertEquals(spadesChar + "Q", queenOfSpades.toString())
        assertEquals(heartsChar + "K", kingOfHearts.toString())
        assertEquals(diamondsChar + "A", aceOfDiamonds.toString())
    }

    @Test
    fun testCardValue(){
        assertEquals(nineOfHearts.cardValue(),9)
        assertEquals(sevenOfClubs.cardValue(),7)
        assertEquals(aceOfDiamonds.cardValue(),11)
    }
}