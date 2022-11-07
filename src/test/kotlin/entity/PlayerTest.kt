package entity
import kotlin.test.*

/**
 * Describes the player.
 */
 class PlayerTest {
    private val player1 : Player = Player("Houzan")


     @Test
     fun testToString(){

         player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.JACK))
         player1.hand.add(Card(CardSuit.HEARTS, CardValue.KING))
         player1.hand.add(Card(CardSuit.CLUBS, CardValue.SEVEN))

         assertEquals(player1.toString(), "Houzan has a list of Cards: "+player1.hand.toString())
     }
}