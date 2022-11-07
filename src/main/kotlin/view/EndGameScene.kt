package view
import service.RootService
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

class EndGameScene (private val rootService: RootService) : MenuScene(400, 1080), Refreshable{

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Game Over",
        font = Font(size = 22)
    )

    private val allPlayersScore = Label(width = 300, height = 140, posX = 50, posY = 115)


    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 255).apply {
    }

    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 325, text = "Quit").apply {
        visual = ColorVisual(Color(221,136,136))
    }

    val newGameButton = Button(width = 140, height = 35, posX = 210, posY = 325, text = "New Game").apply {
        visual = ColorVisual(Color(136, 221, 136))
    }

    init {
        opacity = .5
        addComponents(headlineLabel, allPlayersScore, gameResult, newGameButton, quitButton)
    }

    override fun refreshAfterEndGame() {

        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "No game running" }

        var gameResultString: String
        val winners = rootService.systemService.evaluateWinner()

        if(winners.size == 1){
            gameResultString="${winners.first().playerName} wins the game with score: ${winners.first().score}"
        }
        else{
            gameResultString="Draw: you have the same Score: "
            for(player in winners) {
                gameResultString += "${player.playerName} with score: ${player.score} \n"
            }
        }


        for(player in currentGame.players){
            allPlayersScore.text += "${player.playerName} scored ${player.score} \n"
        }

        gameResult.text=gameResultString
    }
}