package view

import entity.Player
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import service.RootService
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual


class StartScene( private val rootService: RootService) : MenuScene(1920, 1000), Refreshable{

    private val headlineLabel = Label (
        width = 300, height = 50, posX = 800, posY = 50,
        text = "Start New Game",
        font = Font(size = 22)
    )

    private val p1Label = Label(
        width = 300, height = 50,
        posX = 75, posY = 200,
        text = "Player 1:",
        font = Font(size = 22)
    )

    private val p2Label = Label(
        width = 300, height = 50,
        posX = 525, posY = 200,
        text = "Player 2:" ,
        font = Font(size = 22)
    )

    private val p3Label = Label(
        width = 300, height = 50,
        posX = 975, posY = 200,
        text = "Player 3:",
        font = Font(size = 22)
    )

    private val p4Label = Label(
        width = 300, height = 50,
        posX = 1425, posY = 200,
        text = "Player 4:",
        font = Font(size = 22)
    )

    private val alertLabel = Label(
            width = 500, height = 35,
            posX = 700, posY = 750,
            text = "Same name is not possible"

        ).apply {
            isVisible=false
            visual = ColorVisual(221, 136, 136)
        }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 300, height = 45,
        posX = 100, posY = 300,
        text = listOf("Maggie").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank()
            p2Input.isDisabled = this.text.isBlank()
            p3Input.isDisabled = this.text.isBlank()
            p4Input.isDisabled = this.text.isBlank()
        }
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        width = 300, height = 45,
        posX = 550, posY = 300,
        text = listOf("Zoidberg").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank()
            p3Input.isDisabled = this.text.isBlank()
            p4Input.isDisabled = this.text.isBlank()
        }
    }

    private val p3Input: TextField = TextField(
        width = 300, height = 45,
        posX = 1000, posY = 300,
        text = listOf("Leela").random()
    ).apply {
        onKeyTyped = {
            p4Input.isDisabled = this.text.isBlank()
        }
    }

    private val p4Input: TextField = TextField(
        width = 300, height = 45,
        posX = 1450, posY = 300,
        text = listOf("Bender").random()
    )

    val quitButton = Button(
        width = 240, height = 135,
        posX = 600, posY = 700,
        text = "Quit"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        font = Font(40)
    }

    private val startButton = Button(
        width = 240, height = 135,
        posX = 1100, posY = 700,
        text = "Start",
    ).apply {
        visual = ColorVisual(136, 221, 136)
        font = Font(40)
        onMouseClicked = {
            val allNames = listOf<String>(p1Input.text.trim(), p2Input.text.trim(), p3Input.text.trim(), p4Input.text.trim())
            var flag=false
            for(i in 0 until 3){
                for(j in i+1 until 4){
                    if(allNames[i]==allNames[j] && allNames[i].isNotBlank())
                        flag=true
                }
            }
            if(flag){
                alertLabel.isVisible=true
            }
            else{
                val players = ArrayDeque<Player>()

                if(p1Input.text.isNotBlank()){
                    val player1 = Player(p1Input.text.trim())
                    players.add(player1)
                }
                if(p2Input.text.isNotBlank()){
                    val player2 = Player(p2Input.text.trim())
                    players.add(player2)
                }
                if(p3Input.text.isNotBlank() && !p3Input.isDisabled){
                    val player3 = Player(p3Input.text.trim())
                    players.add(player3)
                }
                if(p4Input.text.isNotBlank() && !p4Input.isDisabled){
                    val player4 = Player(p4Input.text.trim())
                    players.add(player4)
                }
                rootService.systemService.startGame(players)
            }
        }
    }

    init {
        opacity = .5
        addComponents(
            headlineLabel,
            alertLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            p3Label, p3Input,
            p4Label, p4Input,
            startButton, quitButton
        )
    }

}