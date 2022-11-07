package service

import entity.*

class SystemService (private val rootService : RootService) : AbstractRefreshingService(){

    fun startGame(players : ArrayDeque<Player>){
        val cards = CardStack()
        cards.putOnTop(defaultRandomCardList())

        val game=Schwimmen(players,cards)
        game.activePlayer=players.first()
        rootService.currentGame=game

        for(p in game.players){
            rootService.playerService.firstDraw(p)
        }
        tableDraw()
        onAllRefreshables { refreshAfterPressStart() }
    }

    /**
     * moves 3 cards from the card stack to the table.
     */
    fun tableDraw() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"No game running currently"}
        currentGame.cards.apply{
             currentGame.table = draw(3)
        }
    }

    /**
     * moves the 3 table's cards to the discarded stack
     * and moves 3 cards from the card stack to the table.
     */
    fun swapAllMidCards(){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"No game running currently"}
        if(!isGameEnded()){
            currentGame.cards.apply{
                for(card in currentGame.table){
                    currentGame.discardStack.putOnTop(card)
                }
                tableDraw()
            }
        }
        if(isGameEnded()){
            onAllRefreshables { refreshAfterEndGame() }
        }
    }

    /**
     * adds a new [player] to the players' array when their number is lesser than 4.
     */
    fun addPlayer(player : Player){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"No game running currently"}
        if(currentGame.players.size<4){
            currentGame.players.addFirst(player)
        }
    }

    /**
     * removes a player from the players' array depending on his [name].
     */
    fun deletePlayer(name : String){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"No game running currently"}
        for(player in currentGame.players){
            if(player.playerName==name){
                currentGame.players.remove(player)
            }
        }
    }

    /**
     * checks  all the possibilities, that lead to the end of the game.
     */
    fun isGameEnded() : Boolean{
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"No game currently running."}

        if(currentGame.cards.size < 3 && rootService.playerService.checkAllPassed()){
            return true
        }
        else if(rootService.playerService.oneHasKnocked() && rootService.playerService.checkAllPassed()){
            return true
        }
        return false
    }


    fun endRound(){
        //Here it comes one more
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        if(!isGameEnded()){
            swapAllMidCards()

            for(p in currentGame.players){
                p.passed=false
            }
            currentGame.activePlayer=currentGame.players[0]

            onAllRefreshables {
                refreshAfterSwitchTableCards()
            }
        }
    }
    /**
     * set active player to the next player if the game is not ended yet
     */
    fun nextPlayer(){
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"No game currently running."}

        if(!isGameEnded()){
            var activePlayerIndex = 0
            for(i in 0 until currentGame.players.size){
                if(currentGame.activePlayer==currentGame.players[i]){
                    activePlayerIndex=i
                }
            }
            if(activePlayerIndex==currentGame.players.size-1){
                currentGame.activePlayer=currentGame.players[0]
                endRound()
            }
            else{
                currentGame.activePlayer=currentGame.players[activePlayerIndex+1]
            }
        }
    }


    /**
     * returns an array of winners, that contains at least one winner.
     */
    fun evaluateWinner() : ArrayDeque<Player>{
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){"No game currently running."}

        for(p in currentGame.players){
            rootService.playerService.updateScore(p)
        }

        var max = currentGame.players.first().score
        var indexOfMax=0
        val winnerList : ArrayDeque<Player> = ArrayDeque()

        for(i in 1 until currentGame.players.size){
            if(currentGame.players[i].score>max){
                max=currentGame.players[i].score
                indexOfMax=i
            }
        }
        winnerList.add(currentGame.players[indexOfMax])
        for(i in 0 until currentGame.players.size){
            if(currentGame.players[i].score==max && i!=indexOfMax){
                winnerList.add(currentGame.players[i])
            }
        }
        return winnerList
    }

    private fun defaultRandomCardList() = List(32) { index ->
        Card(
            CardSuit.values()[index / 8],
            CardValue.values()[(index % 8) + 5]
        )
    }.shuffled()

}