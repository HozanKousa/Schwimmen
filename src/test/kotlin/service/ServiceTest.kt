package service

import entity.*
import org.junit.jupiter.api.Test
import view.Refreshable
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

/**
 * Class that provides tests for [SystemService] and [PlayerService] (both at the same time,
 * as their functionality is not easily separable) by basically playing through some sample games.
 * [TestRefreshable] is used to validate correct refreshing behavior even though no GUI
 * is present.
 */
class ServiceTest {

    /**
     * starts a game with a static order of cards that can be used
     * in other tests to deterministically validate the outcome
     * of turns.
     *
     * @param refreshables refreshables to be added to the root service
     * right after its instantiation (so that, e.g., start new game will already
     * be observable)
     *
     * @return the root service holding the started game as [RootService.currentGame]
     */
    private fun setUpGame(vararg refreshable: Refreshable): RootService {
        val mc = RootService()
        refreshable.forEach { mc.addRefreshable(it) }

        /**
         * Array of [Card] to add later to the card stack
         */
        val cards: ArrayDeque<Card> = ArrayDeque(32)
        cards.add(Card(CardSuit.CLUBS, CardValue.QUEEN))
        cards.add(Card(CardSuit.SPADES, CardValue.TEN))
        cards.add(Card(CardSuit.DIAMONDS, CardValue.SEVEN))
        cards.add(Card(CardSuit.CLUBS, CardValue.EIGHT))
        cards.add(Card(CardSuit.CLUBS, CardValue.NINE))
        cards.add(Card(CardSuit.HEARTS, CardValue.KING))
        cards.add(Card(CardSuit.DIAMONDS, CardValue.QUEEN))
        cards.add(Card(CardSuit.SPADES, CardValue.QUEEN))

        cards.add(Card(CardSuit.DIAMONDS, CardValue.JACK))
        cards.add(Card(CardSuit.SPADES, CardValue.SEVEN))
        cards.add(Card(CardSuit.DIAMONDS, CardValue.KING))
        cards.add(Card(CardSuit.DIAMONDS, CardValue.NINE))
        cards.add(Card(CardSuit.SPADES, CardValue.EIGHT))
        cards.add(Card(CardSuit.HEARTS, CardValue.TEN))
        cards.add(Card(CardSuit.HEARTS, CardValue.EIGHT))
        cards.add(Card(CardSuit.CLUBS, CardValue.JACK))

        cards.add(Card(CardSuit.HEARTS, CardValue.ACE))
        cards.add(Card(CardSuit.SPADES, CardValue.NINE))
        cards.add(Card(CardSuit.CLUBS, CardValue.ACE))
        cards.add(Card(CardSuit.SPADES, CardValue.JACK))
        cards.add(Card(CardSuit.HEARTS, CardValue.SEVEN))
        cards.add( Card(CardSuit.CLUBS, CardValue.SEVEN))
        cards.add( Card(CardSuit.CLUBS, CardValue.KING))
        cards.add( Card(CardSuit.DIAMONDS, CardValue.EIGHT))

        cards.add( Card(CardSuit.CLUBS, CardValue.TEN))
        cards.add( Card(CardSuit.DIAMONDS, CardValue.TEN))
        cards.add( Card(CardSuit.HEARTS, CardValue.JACK))
        cards.add( Card(CardSuit.SPADES, CardValue.KING))
        cards.add( Card(CardSuit.DIAMONDS, CardValue.ACE))
        cards.add( Card(CardSuit.HEARTS, CardValue.QUEEN))
        cards.add( Card(CardSuit.SPADES, CardValue.ACE))
        cards.add( Card(CardSuit.HEARTS, CardValue.NINE))

        /**
         * Array of [Player]
         */
        val players : ArrayDeque<Player> = ArrayDeque()
        players.add(Player("Hozan"))
        players.add(Player("Kousa"))
        players.add(Player("Tim"))

        /**
         * add cards to the card stack
         */
        val cardStack = CardStack()
        cardStack.putOnTop(cards)

        /**
         * make a Schwimmen game and give it both of players array and card stack
         */
        mc.systemService.startGame(players)

        println(mc.currentGame)
        return mc
    }

    /**
     * tests if cards can be given to all players and to the table from the card stack
     */
    @Test
    fun testFirstDraw(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertNotNull(mc.currentGame)
        for(player in mc.currentGame!!.players){
            mc.playerService.firstDraw(player)
        }

        val player1 = mc.currentGame!!.players[0]
        val player2 = mc.currentGame!!.players[1]
        val player3 = mc.currentGame!!.players[2]

        assertEquals(player1.hand.toString(),"[♥9, ♠A, ♥Q]")
        assertEquals(player2.hand.toString(),"[♦A, ♠K, ♥J]")
        assertEquals(player3.hand.toString(),"[♦10, ♣10, ♦8]")

        mc.systemService.tableDraw()
        val tableCards = mc.currentGame!!.table
        assertEquals(tableCards.toString(), "[♣K, ♣7, ♥7]")
    }

    /**
     * tests move all table cards to the discarded stack and draw 3 new cards form the card stack
     */
    @Test
    fun testSwapAllMidCards(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        mc.systemService.tableDraw()

        assertEquals(mc.currentGame!!.table.toString(), "[♥9, ♠A, ♥Q]")

        mc.systemService.swapAllMidCards()

        assertEquals(mc.currentGame!!.table.toString(), "[♦A, ♠K, ♥J]")
        assertEquals(mc.currentGame!!.discardStack.toString(), "[♥Q, ♠A, ♥9]")

    }

    /**
     * tests if a new player can be added to the players array
     */
    @Test
    fun testAddPlayer(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertEquals(mc.currentGame!!.players.size, 3)

        val newPlayer = Player("Fibi")
        mc.systemService.addPlayer(newPlayer)

        assertEquals(mc.currentGame!!.players.size, 4)
        assertSame(mc.currentGame!!.players[0], newPlayer)
    }

    /**
     * tests if a player can be removed from the players array
     */
    @Test
    fun testDeletePlayer(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        mc.systemService.deletePlayer("Kousa")
        assertEquals(mc.currentGame!!.players.size, 2)
    }

    /**
     * tests the possibility of end game when a player knocks and the other passed
     */
    @Test
    fun testIsGameEnded(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        for(player in mc.currentGame!!.players){
            mc.playerService.firstDraw(player)
        }

        mc.systemService.tableDraw()

        mc.playerService.knock()
        mc.playerService.pass()
        mc.playerService.pass()

        assertEquals(mc.systemService.isGameEnded(), true)
    }

    /**
     * defined  winners depending on the values of the cards in their hands
     */
    @Test
    fun testEvaluateWinner(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        for(player in mc.currentGame!!.players){
            mc.playerService.firstDraw(player)
        }
        val player1 = mc.currentGame!!.players[0]
        val player2 = mc.currentGame!!.players[1]
        val player3 = mc.currentGame!!.players[2]

        assertEquals(player1.hand.toString(),"[♥9, ♠A, ♥Q]")
        assertEquals(player2.hand.toString(),"[♦A, ♠K, ♥J]")
        assertEquals(player3.hand.toString(),"[♦10, ♣10, ♦8]")

        val winners = mc.systemService.evaluateWinner()

        assertEquals(winners.size,1)
        assertEquals(winners.first(), player2)

    }

    /**
     * tests if switch a card between player and table is possible
     */
    @Test
    fun testSwapOneCard(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertNotNull(mc.currentGame)
        for(player in mc.currentGame!!.players){
            mc.playerService.firstDraw(player)
        }

        val player1 = mc.currentGame!!.players[0]
        val player2 = mc.currentGame!!.players[1]
        val player3 = mc.currentGame!!.players[2]

        assertEquals(player1.hand.toString(),"[♥9, ♠A, ♥Q]")
        assertEquals(player2.hand.toString(),"[♦A, ♠K, ♥J]")
        assertEquals(player3.hand.toString(),"[♦10, ♣10, ♦8]")

        mc.systemService.tableDraw()
        val tableCards = mc.currentGame!!.table
        assertEquals(tableCards.toString(), "[♣K, ♣7, ♥7]")

        mc.playerService.swapOneCard(Card(CardSuit.HEARTS, CardValue.NINE), Card(CardSuit.CLUBS, CardValue.KING))

        assertEquals(player1.hand.toString(),"[♣K, ♠A, ♥Q]")
        assertEquals(tableCards.toString(), "[♥9, ♣7, ♥7]")
    }

    /**
     * tests if switch all cards between player and table is possible
     */
    @Test
    fun testSwapAllCards(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertNotNull(mc.currentGame)
        for(player in mc.currentGame!!.players){
            mc.playerService.firstDraw(player)
        }

        assertEquals(mc.currentGame!!.players[0].hand.toString(),"[♥9, ♠A, ♥Q]")
        assertEquals(mc.currentGame!!.players[1].hand.toString(),"[♦A, ♠K, ♥J]")
        assertEquals(mc.currentGame!!.players[2].hand.toString(),"[♦10, ♣10, ♦8]")

        mc.systemService.tableDraw()
        assertEquals(mc.currentGame!!.table.toString(), "[♣K, ♣7, ♥7]")

        mc.playerService.swapAllCards()

        assertEquals(mc.currentGame!!.players[0].hand.toString(),"[♣K, ♣7, ♥7]")
        assertEquals(mc.currentGame!!.table.toString(), "[♦A, ♠K, ♥J]")
    }

    @Test
    fun testNextPlayer(){

    }


}