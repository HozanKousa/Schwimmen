package view

import entity.Player
import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

/**
 * Implementation of the BGW [BoardGameApplication] for the example card game "War"
 */
class SchwimmenApplication : BoardGameApplication("Schwimmen"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()

    // Scenes

    // This is where the actual game takes place
    private val gameScene = GameScene(rootService)

    // This menu scene is shown after each finished game (i.e. no more cards to draw)
    private val gameFinishedMenuScene = EndGameScene(rootService).apply {
        newGameButton.onMouseClicked = {
            this@SchwimmenApplication.showMenuScene(newGameMenuScene)
        }
        quitButton.onMouseClicked = {
            exit()
        }
    }

    // This menu scene is shown after application start and if the "new game" button
    // is clicked in the gameFinishedMenuScene
    private val newGameMenuScene = StartScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    init {

        // all scenes and the application itself need too
        // react to changes done in the service layer
        rootService.addRefreshables(
            this,
            gameScene,
            gameFinishedMenuScene,
            newGameMenuScene
        )

        // This is just done so that the blurred background when showing
        // the new game menu has content and looks nicer
        val players = ArrayDeque<Player>()
        players.add(Player("Hozan"))
        players.add(Player("Kousa"))

        rootService.systemService.startGame(players)

        this.showGameScene(gameScene)
        this.showMenuScene(newGameMenuScene, 0)

    }

    override fun refreshAfterPressStart() {
        this.hideMenuScene()
    }

    override fun refreshAfterEndGame() {
        this.showMenuScene(gameFinishedMenuScene)
    }


}

