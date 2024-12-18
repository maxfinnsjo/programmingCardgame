package com.programmingcardgame.service;

import com.programmingcardgame.model.Card;
import com.programmingcardgame.model.CardEffect;
import com.programmingcardgame.model.GameGoal;
import com.programmingcardgame.model.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(
        GameService.class
    );

    @Autowired
    private DeckService deckService;

    @Autowired
    private RuleService ruleService;

    // Initiera players-listan direkt
    private List<Player> players = new ArrayList<>();
    private GameGoal currentGoal;
    private String currentPlayerName;

    public void initializeGame(List<String> playerNames) {
        if (playerNames == null || playerNames.isEmpty()) {
            throw new IllegalArgumentException(
                "Player names cannot be null or empty"
            );
        }

        logger.info("Initializing game with {} players", playerNames.size());
        players = new ArrayList<>();

        try {
            for (String name : playerNames) {
                Player player = new Player();
                player.setName(name);
                player.setHealth(100);
                player.setEnergy(10);

                // Dela ut starthand
                for (int i = 0; i < 5; i++) {
                    Card drawnCard = deckService.drawCard();
                    logger.debug(
                        "Player {} drew card: {}",
                        name,
                        drawnCard.getName()
                    );
                    player.drawCard(drawnCard);
                }

                players.add(player);
            }

            // Sätt första spelaren som aktiv
            currentPlayerName = players.get(0).getName();

            // Blanda däcket
            deckService.shuffleDeck();
            logger.info(
                "Game initialized successfully, {} starts",
                currentPlayerName
            );
        } catch (Exception e) {
            logger.error("Error during game initialization", e);
            throw new RuntimeException("Failed to initialize game", e);
        }
    }

    public DeckService getDeckService() {
        return deckService;
    }

    public void startNewRound() {
        logger.info("Starting new round");
        try {
            // Återställ spelarnas energi
            players.forEach(player -> player.setEnergy(10));

            // Dela ut nya kort
            players.forEach(player -> {
                while (player.getHand().size() < 5) {
                    player.drawCard(deckService.drawCard());
                }
            });

            // Sätt första spelaren som aktiv för nya rundan
            currentPlayerName = players.get(0).getName();

            logger.info(
                "New round started successfully, {} goes first",
                currentPlayerName
            );
        } catch (Exception e) {
            logger.error("Error starting new round", e);
            throw new RuntimeException("Failed to start new round", e);
        }
    }

    public void endTurn(String playerName) {
        if (!playerName.equals(currentPlayerName)) {
            throw new IllegalStateException("Not your turn!");
        }

        Player currentPlayer = findPlayerByName(playerName);
        currentPlayer.endTurn();

        // Byt till nästa spelare
        Player opponent = getOpponent(currentPlayer);
        currentPlayerName = opponent.getName();
        opponent.startTurn();

        logger.info(
            "Turn ended for {}, next player: {}",
            playerName,
            currentPlayerName
        );
    }

    public void playCard(String playerName, String cardName) {
        try {
            if (!playerName.equals(currentPlayerName)) {
                logger.warn("Player {} tried to play out of turn", playerName);
                throw new IllegalStateException(
                    "Not your turn! Current player: " + currentPlayerName
                );
            }

            Player currentPlayer = findPlayerByName(playerName);
            Card card = findCardInHand(currentPlayer, cardName);

            if (card == null) {
                logger.warn(
                    "Card {} not found in player {}'s hand",
                    cardName,
                    playerName
                );
                throw new IllegalArgumentException(
                    "Card not found in player's hand"
                );
            }

            if (!ruleService.canPlayCard(currentPlayer, card)) {
                logger.warn(
                    "Player {} cannot play card {}",
                    playerName,
                    cardName
                );
                throw new IllegalStateException("Cannot play this card");
            }

            Player opponent = getOpponent(currentPlayer);
            ruleService.applyCardEffect(currentPlayer, opponent, card);
            currentPlayer.playCard(card);

            // Kontrollera vinstvillkor efter att kort spelats
            if (ruleService.checkWinCondition(currentPlayer)) {
                logger.info("Player {} has won the game!", playerName);
                throw new GameWonException(playerName + " has won the game!");
            }

            // Byt till nästa spelare
            currentPlayerName = opponent.getName();

            logger.info(
                "Player {} successfully played card {}, next player: {}",
                playerName,
                cardName,
                currentPlayerName
            );
        } catch (GameWonException e) {
            throw e; // Låt denna bubbla upp för speciell hantering
        } catch (Exception e) {
            logger.error("Error while playing card", e);
            throw new RuntimeException(
                "Failed to play card: " + e.getMessage(),
                e
            );
        }
    }

    public Player findPlayerByName(String name) {
        return players
            .stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Player not found: " + name)
            );
    }

    private Card findCardInHand(Player player, String cardName) {
        return player
            .getHand()
            .stream()
            .filter(c -> c.getName().equals(cardName))
            .findFirst()
            .orElse(null);
    }

    private Player getOpponent(Player currentPlayer) {
        return players
            .stream()
            .filter(p -> !p.equals(currentPlayer))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No opponent found"));
    }

    public List<Player> getPlayers() {
        // Lägg till null-check
        if (players == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(players);
    }

    public String getCurrentPlayerName() {
        // Lägg till null-check
        return currentPlayerName != null ? currentPlayerName : "";
    }

    public GameGoal getCurrentGoal() {
        // Lägg till null-check
        return currentGoal;
    }

    // Intern klass för att hantera vinst
    public static class GameWonException extends RuntimeException {

        public GameWonException(String message) {
            super(message);
        }
    }

    public boolean isGameInitialized() {
        return players != null && !players.isEmpty();
    }
}
