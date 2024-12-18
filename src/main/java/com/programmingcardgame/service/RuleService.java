package com.programmingcardgame.service;

import com.programmingcardgame.model.Card;
import com.programmingcardgame.model.CardEffect;
import com.programmingcardgame.model.GameGoal;
import com.programmingcardgame.model.Player;
import java.util.List; // Lägg till denna import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private static final Logger logger = LoggerFactory.getLogger(
        RuleService.class
    );

    @Autowired
    private DeckService deckService;

    public void applyCardEffect(Player player, Player opponent, Card card) {
        // Minska spelarens energi
        player.setEnergy(player.getEnergy() - card.getEnergyCost());
        logger.debug(
            "Player {} spent {} energy, remaining: {}",
            player.getName(),
            card.getEnergyCost(),
            player.getEnergy()
        );

        // Applicera alla kortets effekter
        for (CardEffect effect : card.getEffects()) {
            switch (effect.getType()) {
                case DAMAGE:
                    if ("opponent".equals(effect.getTarget())) {
                        int newHealth =
                            opponent.getHealth() - effect.getValue();
                        opponent.setHealth(newHealth);
                        logger.debug(
                            "Player {} dealt {} damage to {}, remaining health: {}",
                            player.getName(),
                            effect.getValue(),
                            opponent.getName(),
                            newHealth
                        );
                    }
                    break;
                case HEAL:
                    if ("self".equals(effect.getTarget())) {
                        int newHealth = Math.min(
                            100,
                            player.getHealth() + effect.getValue()
                        );
                        player.setHealth(newHealth);
                        logger.debug(
                            "Player {} healed for {}, new health: {}",
                            player.getName(),
                            effect.getValue(),
                            newHealth
                        );
                    }
                    break;
                case DRAW_CARD:
                    if ("self".equals(effect.getTarget())) {
                        for (int i = 0; i < effect.getValue(); i++) {
                            Card drawnCard = deckService.drawCard();
                            player.drawCard(drawnCard);
                            logger.debug(
                                "Player {} drew card: {}",
                                player.getName(),
                                drawnCard.getName()
                            );
                        }
                    }
                    break;
                case GAIN_ENERGY:
                    if ("self".equals(effect.getTarget())) {
                        int newEnergy = player.getEnergy() + effect.getValue();
                        player.setEnergy(newEnergy);
                        logger.debug(
                            "Player {} gained {} energy, new total: {}",
                            player.getName(),
                            effect.getValue(),
                            newEnergy
                        );
                    }
                    break;
                case MODIFY_COMPLEXITY:
                    // Detta kommer användas för att bygga upp programmeringskombinationer
                    logger.debug(
                        "Complexity modified by {} for {}",
                        effect.getValue(),
                        effect.getTarget()
                    );
                    break;
                case COPY_CARD:
                    if ("self".equals(effect.getTarget())) {
                        // Implementera kopiering av kort här
                        logger.debug(
                            "Card copy effect triggered for {}",
                            player.getName()
                        );
                    }
                    break;
                case TRANSFORM_CARD:
                    // Implementera transformering av kort här
                    logger.debug("Card transform effect triggered");
                    break;
            }
        }
    }

    public boolean canPlayCard(Player player, Card card) {
        // Grundläggande energikontroll
        if (player.getEnergy() < card.getEnergyCost()) {
            logger.debug(
                "Player {} cannot play {} - insufficient energy ({}/{})",
                player.getName(),
                card.getName(),
                player.getEnergy(),
                card.getEnergyCost()
            );
            return false;
        }

        // Kontrollera eventuella krav på kortet
        if (!validateRequirements(player, card)) {
            logger.debug(
                "Player {} cannot play {} - requirements not met",
                player.getName(),
                card.getName()
            );
            return false;
        }

        return true;
    }

    private boolean validateRequirements(Player player, Card card) {
        if (
            card.getRequirements() == null || card.getRequirements().isEmpty()
        ) {
            return true;
        }

        // Här kan vi lägga till mer avancerade krav
        // Till exempel att vissa kort måste spelas före andra
        return true;
    }

    public boolean checkWinCondition(Player player) {
        // Kontrollera om motståndaren har förlorat (hälsa <= 0)
        for (Card playedCard : player.getPlayedCards()) {
            // Räkna komplexitet och andra vinstrelaterade värden
            logger.debug(
                "Checking win conditions for played card: {}",
                playedCard.getName()
            );
        }

        // Implementera andra vinstregler här
        // Till exempel: uppnått viss komplexitet, byggt specifika kombinationer, etc.

        return false; // Placeholder - implementera faktiska vinstregler
    }

    public int calculateCombinationValue(List<Card> cards) {
        return cards.stream().mapToInt(card -> card.getComplexity()).sum();
    }
}
