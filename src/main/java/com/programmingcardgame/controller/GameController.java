package com.programmingcardgame.controller;

import com.programmingcardgame.model.Card;
import com.programmingcardgame.model.Player;
import com.programmingcardgame.service.GameService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*") // För utveckling, ändra detta i produktion
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(
        GameController.class
    );

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody List<String> playerNames) {
        try {
            gameService.initializeGame(playerNames);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Game started successfully");
            response.put("players", gameService.getPlayers());
            response.put("currentPlayer", gameService.getCurrentPlayerName());
            response.put("currentGoal", gameService.getCurrentGoal());

            logger.info("Game started with players: {}", playerNames);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to start game", e);
            return ResponseEntity.badRequest()
                .body(
                    Map.of(
                        "error",
                        "Failed to start game",
                        "message",
                        e.getMessage()
                    )
                );
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getGameStatus() {
        try {
            if (!gameService.isGameInitialized()) {
                return ResponseEntity.ok(
                    Map.of(
                        "message",
                        "Game not started",
                        "status",
                        "NOT_INITIALIZED"
                    )
                );
            }

            Map<String, Object> status = new HashMap<>();
            status.put("players", gameService.getPlayers());
            status.put("currentPlayer", gameService.getCurrentPlayerName());
            status.put("currentGoal", gameService.getCurrentGoal());
            status.put("status", "ACTIVE");

            logger.debug("Game status requested");
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Failed to get game status", e);
            return ResponseEntity.badRequest()
                .body(
                    Map.of(
                        "error",
                        "Failed to get game status",
                        "message",
                        e.getMessage()
                    )
                );
        }
    }

    @GetMapping("/player/{playerName}")
    public ResponseEntity<?> getPlayerStatus(@PathVariable String playerName) {
        try {
            Player player = gameService.findPlayerByName(playerName);
            Map<String, Object> playerStatus = new HashMap<>();
            playerStatus.put("player", player);
            playerStatus.put(
                "isCurrentTurn",
                playerName.equals(gameService.getCurrentPlayerName())
            );

            logger.debug("Player status requested for: {}", playerName);
            return ResponseEntity.ok(playerStatus);
        } catch (Exception e) {
            logger.error("Failed to get player status", e);
            return ResponseEntity.badRequest()
                .body(
                    Map.of(
                        "error",
                        "Failed to get player status",
                        "message",
                        e.getMessage()
                    )
                );
        }
    }

    @PostMapping("/play")
    public ResponseEntity<?> playCard(
        @RequestParam String playerName,
        @RequestParam String cardName
    ) {
        try {
            gameService.playCard(playerName, cardName);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Card played successfully");
            response.put("players", gameService.getPlayers());
            response.put("currentPlayer", gameService.getCurrentPlayerName());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            String errorMessage = e.getMessage();
            if (errorMessage.contains("energy")) {
                errorMessage = "Not enough energy to play " + cardName;
            }
            errorResponse.put("error", errorMessage);
            errorResponse.put("players", gameService.getPlayers());
            errorResponse.put(
                "currentPlayer",
                gameService.getCurrentPlayerName()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/next-round")
    public ResponseEntity<?> startNewRound() {
        try {
            gameService.startNewRound();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "New round started");
            response.put("players", gameService.getPlayers());
            response.put("currentPlayer", gameService.getCurrentPlayerName());
            response.put("currentGoal", gameService.getCurrentGoal());

            logger.info("New round started");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to start new round", e);
            return ResponseEntity.badRequest()
                .body(
                    Map.of(
                        "error",
                        "Failed to start new round",
                        "message",
                        e.getMessage()
                    )
                );
        }
    }

    @PostMapping("/end-turn")
    public ResponseEntity<?> endTurn(@RequestParam String playerName) {
        try {
            gameService.endTurn(playerName);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Turn ended");
            response.put("players", gameService.getPlayers());
            response.put("currentPlayer", gameService.getCurrentPlayerName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // Hjälpendpoint
    @GetMapping("/debug/deck")
    public ResponseEntity<?> getDeckInfo() {
        try {
            return ResponseEntity.ok(
                Map.of(
                    "remainingCards",
                    gameService.getDeckService().getRemainingCards()
                )
            );
        } catch (Exception e) {
            logger.error("Failed to get deck info", e);
            return ResponseEntity.badRequest()
                .body(
                    Map.of(
                        "error",
                        "Failed to get deck info",
                        "message",
                        e.getMessage()
                    )
                );
        }
    }
}
