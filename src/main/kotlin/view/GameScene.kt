package view


import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.components.gamecomponentviews.CardView
import service.RootService
import entity.*
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class GameScene (private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable{

    // Player 1 (bottom of screen) cards
    private val player1FirstCard = LabeledStackView(posX = 745, posY = 840, "Player 1 \nFirst Card")
    private val player1SecondCard = LabeledStackView(posX = 895, posY = 840, "Player 1 \nSecond Card")
    private val player1ThirdCard = LabeledStackView(posX = 1050, posY = 840, "Player 1 \nThird Card")

    // Player 2 (Top of screen) cards
    private val player2FirstCard = LabeledStackView(posX = 745, posY = 50, "Player 2 \nFirst Card")
    private val player2SecondCard = LabeledStackView(posX = 895, posY = 50, "Player 2 \nSecond Card")
    private val player2ThirdCard = LabeledStackView(posX = 1050, posY = 50, "Player 2 \nThird Card")

    // Player 3 (left of screen) cards
    private val player3FirstCard = LabeledStackView(posX = 50, posY = 250, "Player 3 \nFirst Card").apply {
        isVisible=false
    }
    private val player3SecondCard = LabeledStackView(posX = 50, posY = 470, "Player 3 \nSecond Card").apply {
        isVisible=false
    }
    private val player3ThirdCard = LabeledStackView(posX = 50, posY = 690, "Player 3 \nThird Card").apply {
        isVisible=false
    }

    // Player 4 (right of screen) cards
    private val player4FirstCard = LabeledStackView(posX = 1700, posY = 250, "Player 4 \nFirst Card").apply {
        isVisible=false
    }
    private val player4SecondCard = LabeledStackView(posX = 1700, posY = 470, "Player 4 \nSecond Card").apply {
        isVisible=false
    }
    private val player4ThirdCard = LabeledStackView(posX = 1700, posY = 690, "Player 4 \nThird Card").apply {
        isVisible=false
    }

    private val tableCard1 = LabeledStackView(posX = 700, posY = 450, "Table First Card")
    private val tableCard2 = LabeledStackView(posX = 900, posY = 450, "Table Second Card")
    private val tableCard3 = LabeledStackView(posX = 1100, posY = 450, "Table Third Card")

    private val drawStack = LabeledStackView(posX = 1400, posY = 450, "Draw Stack")
    private val discardStack = LabeledStackView(posX = 400, posY = 450, "Discard Stack")

    private val knockButton = Button(
        width = 140, height = 35,
        posX = 410, posY = 900,
        text = "K"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        width = 120.0
        height = 120.0
        font = Font(40)
        onMouseClicked = {
            rootService.playerService.knock()
            refreshAfterKnock()
        }
    }

    private val passButton = Button(
        width = 140, height = 35,
        posX = 560, posY = 900,
        text = "P"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        width = 120.0
        height = 120.0
        font = Font(40)
        onMouseClicked = {
            rootService.playerService.pass()
            refreshAfterPass()
        }
    }

    private val swapOne = Button(
        width = 140, height = 35,
        posX = 1250, posY = 900,
        text = "1"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        width = 120.0
        height = 120.0
        font = Font(40)
    }

    private val swapAll = Button(
        width = 140, height = 35,
        posX = 1400, posY = 900,
        text = "3"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        width = 120.0
        height = 120.0
        font = Font(40)
        onMouseClicked = {
            rootService.playerService.swapAllCards()
            refreshAfterSwapAllCards()
        }
    }

    /**
     * structure to hold pairs of (card, cardView) that can be used
     *
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     *
     * 2. to find the corresponding card to pass to a service method on the occurrence of
     * ui events on views (backward lookup).
     */
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    override fun refreshAfterPressStart() {
        val currentGame=rootService.currentGame
        checkNotNull(currentGame){"No Game is running"}

        cardMap.clear()

        val cardImageLoader = CardImageLoader()



        initializeStackView(currentGame.cards, drawStack, cardImageLoader)
        initializeStackView(currentGame.discardStack, discardStack, cardImageLoader)

        initializeCardView(currentGame.players[0].hand[0], player1FirstCard, cardImageLoader)
        initializeCardView(currentGame.players[0].hand[1], player1SecondCard, cardImageLoader)
        initializeCardView(currentGame.players[0].hand[2], player1ThirdCard, cardImageLoader)

        initializeCardView(currentGame.players[1].hand[0], player2FirstCard, cardImageLoader)
        initializeCardView(currentGame.players[1].hand[1], player2SecondCard, cardImageLoader)
        initializeCardView(currentGame.players[1].hand[2], player2ThirdCard, cardImageLoader)

        initializeCardView(currentGame.cards.cards[0], tableCard1, cardImageLoader)
        initializeCardView(currentGame.cards.cards[1], tableCard2, cardImageLoader)
        initializeCardView(currentGame.cards.cards[2], tableCard3, cardImageLoader)


        moveCardView(cardMap.forward(currentGame.cards.cards[0]), tableCard1, true)
        moveCardView(cardMap.forward(currentGame.cards.cards[1]), tableCard2, true)
        moveCardView(cardMap.forward(currentGame.cards.cards[2]), tableCard3, true)

        moveCardView(cardMap.forward(currentGame.players[0].hand[0]), player1FirstCard, true)
        moveCardView(cardMap.forward(currentGame.players[0].hand[1]), player1SecondCard, true)
        moveCardView(cardMap.forward(currentGame.players[0].hand[2]), player1ThirdCard, true)

        discardStack.clear()


        if (currentGame.players.size==3){
                player3FirstCard.isVisible=true
                player3SecondCard.isVisible=true
                player3ThirdCard.isVisible=true
                initializeCardView(currentGame.players[2].hand[0], player3FirstCard, cardImageLoader)
                initializeCardView(currentGame.players[2].hand[1], player3SecondCard, cardImageLoader)
                initializeCardView(currentGame.players[2].hand[2], player3ThirdCard, cardImageLoader)

                player3FirstCard.onMousePressed = {
                    moveCardView(cardMap.forward(currentGame.players[2].hand[0]), player3FirstCard, true)
                }
                player3SecondCard.onMousePressed = {
                    moveCardView(cardMap.forward(currentGame.players[2].hand[1]), player3SecondCard, true)
                }
                player3ThirdCard.onMousePressed = {
                    moveCardView(cardMap.forward(currentGame.players[2].hand[2]), player3ThirdCard, true)
                }
        }
        if (currentGame.players.size==4){
            player3FirstCard.isVisible=true
            player3SecondCard.isVisible=true
            player3ThirdCard.isVisible=true

            player4FirstCard.isVisible=true
            player4SecondCard.isVisible=true
            player4ThirdCard.isVisible=true

            initializeCardView(currentGame.players[2].hand[0], player3FirstCard, cardImageLoader)
            initializeCardView(currentGame.players[2].hand[1], player3SecondCard, cardImageLoader)
            initializeCardView(currentGame.players[2].hand[2], player3ThirdCard, cardImageLoader)

            initializeCardView(currentGame.players[3].hand[0], player4FirstCard, cardImageLoader)
            initializeCardView(currentGame.players[3].hand[1], player4SecondCard, cardImageLoader)
            initializeCardView(currentGame.players[3].hand[2], player4ThirdCard, cardImageLoader)


            player2FirstCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[1].hand[0]), player2FirstCard, true)
            }
            player2SecondCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[1].hand[1]), player2SecondCard, true)
            }
            player2ThirdCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[1].hand[2]), player2ThirdCard, true)
            }

            player3FirstCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[2].hand[0]), player3FirstCard, true)
            }
            player3SecondCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[2].hand[1]), player3SecondCard, true)
            }
            player3ThirdCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[2].hand[2]), player3ThirdCard, true)
            }

            player4FirstCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[3].hand[0]), player4FirstCard, true)
            }
            player4SecondCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[3].hand[1]), player4SecondCard, true)
            }
            player4ThirdCard.onMousePressed = {
                moveCardView(cardMap.forward(currentGame.players[3].hand[2]), player4ThirdCard, true)
            }
        }

    }

    private fun initializeStackView(stack: CardStack, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()
        stack.peekAll().reversed().forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(card.type, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    private fun initializeCardView(card: Card, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
      val cardView = CardView(
          height = 200,
          width = 130,
          front = ImageVisual(cardImageLoader.frontImageFor(card.type, card.value)),
          back = ImageVisual(cardImageLoader.backImage)
      )
        stackView.add(cardView)
        cardMap.add(card to cardView)
    }


    private fun moveCardView(cardView: CardView, toStack: LabeledStackView, flip: Boolean = false) {
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }
        cardView.removeFromParent()
        toStack.add(cardView)
    }


    override fun refreshAfterSwitchTableCards() {
        val currentGame=rootService.currentGame
        checkNotNull(currentGame){"No Game is running"}
        val cardImageLoader = CardImageLoader()

        initializeCardView(currentGame.cards.cards[0], tableCard1, cardImageLoader)
        initializeCardView(currentGame.cards.cards[1], tableCard2, cardImageLoader)
        initializeCardView(currentGame.cards.cards[2], tableCard3, cardImageLoader)

        initializeStackView(currentGame.discardStack, discardStack, cardImageLoader)

        moveCardView(cardMap.forward(currentGame.cards.cards[0]), tableCard1, true)
        moveCardView(cardMap.forward(currentGame.cards.cards[1]), tableCard2, true)
        moveCardView(cardMap.forward(currentGame.cards.cards[2]), tableCard3, true)
    }

    override fun refreshAfterSwapAllCards() {

    }


    /**
     * Initializes the complete GUI, i.e. the four stack views (left, right, played,
     * collected) of each player.
     */
    init {
        // dark green for "Casino table" flair
        background = ColorVisual(108, 168, 59)

        addComponents(
            player1FirstCard,
            player1SecondCard,
            player1ThirdCard,

            player2FirstCard,
            player2SecondCard,
            player2ThirdCard,

            player3FirstCard,
            player3SecondCard,
            player3ThirdCard,

            player4FirstCard,
            player4SecondCard,
            player4ThirdCard,

            tableCard1,
            tableCard2,
            tableCard3,
            drawStack,
            discardStack,
            knockButton,
            passButton,
            swapOne,
            swapAll
        )

    }
}