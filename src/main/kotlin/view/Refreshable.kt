package view

import entity.Player

interface Refreshable {

    fun refreshAfterCreatePlayer() {}

    fun refreshAfterDeletePlayer() {}

    fun refreshAfterPressStart() {}

    fun refreshAfterSwapOneCard() {}

    fun refreshAfterSwapAllCards() {}

    fun refreshAfterPass(){}

    fun refreshAfterKnock(){}

    fun refreshAfterSwitchTableCards(){}

    fun refreshAfterEndRound(){}

    fun refreshAfterEndGame(){}

}