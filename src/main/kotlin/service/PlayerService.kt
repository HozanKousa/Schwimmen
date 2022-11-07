package service

import entity.Card
import entity.CardSuit
import entity.Player

/**
 * Service layer class that provides the logic for the possible actions a player
 * can take in the game.
 */
class PlayerService (private val rootService : RootService) : AbstractRefreshingService(){

    /**
     * takes 3 cards from the card stack and add them to the [player]'s hand.
     */
    fun firstDraw(player : Player){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        currentGame.cards.apply{
            player.hand = draw(3)
        }
    }

    /**
     * sets the status of player's passed to true.
     * and checks if all players have passed and none of them has knocked, then ends the round.
     */
    fun pass(){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        currentGame.activePlayer.passed=true
        rootService.systemService.nextPlayer()
    }

    /**
     * sets the status of player's knocked & passed to true.
     */
    fun knock(){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        currentGame.activePlayer.knocked=true
        rootService.systemService.nextPlayer()
    }

    /**
     * switches the chosen cards between player's hand and the table.
     */
    fun swapOneCard(handCard : Card, tableCard : Card){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val activePlayer=currentGame.activePlayer

        for (t in currentGame.table){
            if(t==tableCard){
                for (h in activePlayer.hand){
                    if(h==handCard){
                        val temp = t
                        currentGame.table.remove(t)
                        currentGame.table.addFirst(h)
                        activePlayer.hand.remove(h)
                        activePlayer.hand.addFirst(temp)
                    }
                }
            }
        }
        onAllRefreshables {
            refreshAfterSwapOneCard()
        }
    }

    /**
     * switches all the cards between player's hand and the table.
     */
    fun swapAllCards(){

        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        val activePlayer=currentGame.activePlayer

        val temp = activePlayer.hand
        activePlayer.hand=currentGame.table
        currentGame.table = temp

        onAllRefreshables {
            refreshAfterSwapAllCards()
        }
    }

    /**
     * moves all the cards from the table to the discarded stack and sets all players' status o false.
     */


    /**
     * checks if all the players have passed.
     */
    fun checkAllPassed() : Boolean{
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        for(p in currentGame.players){
            if(!p.passed){
                return false
            }
        }
        return true
    }

    /**
     * checks if at least one player has knocked.
     */
     fun oneHasKnocked() : Boolean{
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        for(p in currentGame.players){
            if(p.knocked){
                return true
            }
        }
        return false
    }

    /**
     * returns the [player]'s points depending on the value of the cards in his hand.
     */
    fun updateScore(player : Player){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        val hand = player.hand

        if((hand[0].value == hand[1].value) && (hand[1].value == hand[2].value)){
            player.score=30.5
        }
        else{
            var spadesScore = 0
            var clubsScore = 0
            var heartsScore = 0
            var diamondsScore = 0

            for(card in hand){
                when (card.type) {
                    CardSuit.SPADES -> spadesScore += card.cardValue()
                    CardSuit.CLUBS -> clubsScore += card.cardValue()
                    CardSuit.HEARTS -> heartsScore += card.cardValue()
                    CardSuit.DIAMONDS -> diamondsScore += card.cardValue()
                }
            }
            player.score= maxOf(spadesScore, clubsScore, heartsScore, diamondsScore).toDouble()
        }
    }



}