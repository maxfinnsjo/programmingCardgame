package com.programmingcardgame.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.programmingcardgame.model.Card;
import com.programmingcardgame.model.Player;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GameServiceTest {

    @Mock
    private DeckService deckService;

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void initializeGame_WithValidPlayers_ShouldCreatePlayers() {
        // Given
        List<String> playerNames = Arrays.asList("Player1", "Player2");
        when(deckService.drawCard()).thenReturn(new Card());

        // When
        gameService.initializeGame(playerNames);

        // Then
        List<Player> players = gameService.getPlayers();
        assertEquals(2, players.size());
        assertEquals("Player1", players.get(0).getName());
        assertEquals("Player2", players.get(1).getName());
    }

    @Test
    void initializeGame_WithNullPlayerNames_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.initializeGame(null);
        });
    }
}
