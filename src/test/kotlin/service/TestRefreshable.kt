package service

import entity.Player
import view.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable : Refreshable {
    var refreshAfterPressStartCalled: Boolean = false
        private set

    var refreshAfterCreatePlayerCalled: Boolean = false
        private set

    var refreshAfterDeletePlayerCalled: Boolean = false
        private set

    var refreshAfterSwapOneCardCalled: Boolean = false
        private set

    var refreshAfterSwapAllCardsCalled: Boolean = false
        private set

    var refreshAfterPassCalled: Boolean = false
        private set

    var refreshAfterKnockCalled: Boolean = false
        private set

    var refreshAfterSwitchTableCardsCalled: Boolean = false
        private set

    var refreshAfterEndRoundCalled: Boolean = false
        private set

    var refreshAfterEndGameCalled: Boolean = false
        private set

    /**
     * resets all *Called properties to false
     */
    fun reset() {
        refreshAfterPressStartCalled = false
        refreshAfterCreatePlayerCalled = false
        refreshAfterDeletePlayerCalled = false
        refreshAfterSwapOneCardCalled = false
        refreshAfterSwapAllCardsCalled = false
        refreshAfterPassCalled = false
        refreshAfterKnockCalled = false
        refreshAfterSwitchTableCardsCalled = false
        refreshAfterEndRoundCalled = false
        refreshAfterEndGameCalled = false
    }

    override fun refreshAfterPressStart() {
        refreshAfterPressStartCalled = true
    }

    override fun refreshAfterCreatePlayer() {
        refreshAfterCreatePlayerCalled = true
    }

    override fun refreshAfterDeletePlayer() {
        refreshAfterDeletePlayerCalled = true
    }

    override fun refreshAfterSwapOneCard() {
        refreshAfterSwapOneCardCalled = true
    }

    override fun refreshAfterSwapAllCards() {
        refreshAfterSwapAllCardsCalled = true
    }

    override fun refreshAfterPass() {
        refreshAfterPassCalled = true
    }

    override fun refreshAfterKnock() {
        refreshAfterKnockCalled = true
    }

    override fun refreshAfterSwitchTableCards() {
        refreshAfterSwitchTableCardsCalled = true
    }

    override fun refreshAfterEndRound() {
        refreshAfterEndRoundCalled = true
    }

    override fun refreshAfterEndGame() {
        refreshAfterEndGameCalled = true
    }
}