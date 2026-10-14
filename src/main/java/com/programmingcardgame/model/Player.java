package com.programmingcardgame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player {

    @JsonIgnore
    private final transient Object lock = new Object();

    private String name;
    private int health;
    private int energy;
    private int totalComplexity;
    private boolean hasPlayedCardThisTurn;

    @Builder.Default
    private List<Card> hand = new ArrayList<>();

    @Builder.Default
    private List<Card> playedCards = new ArrayList<>();

    @Builder.Default
    private List<Card> discardPile = new ArrayList<>();

    @Builder.Default
    private List<String> activeEffects = new ArrayList<>();

    public void drawCard(Card card) {
        synchronized (lock) {
            if (card != null) {
                hand.add(card);
            }
        }
    }

    public void playCard(Card card) {
        synchronized (lock) {
            if (hand.contains(card)) {
                hand.remove(card);
                playedCards.add(card);
                totalComplexity += card.getComplexity();
                hasPlayedCardThisTurn = true;
            }
        }
    }

    public void discardCard(Card card) {
        synchronized (lock) {
            if (hand.contains(card)) {
                hand.remove(card);
                discardPile.add(card);
            }
        }
    }

    public void discardHand() {
        synchronized (lock) {
            discardPile.addAll(hand);
            hand.clear();
        }
    }

    public void startTurn() {
        energy = 10; // Reset energy at start of turn
        hasPlayedCardThisTurn = false;
        // Process any start-of-turn effects here
    }

    public void endTurn() {
        // Process any end-of-turn effects here
        hasPlayedCardThisTurn = false;
    }

    public boolean canPlayCard(Card card) {
        return energy >= card.getEnergyCost() && !hasPlayedCardThisTurn;
    }

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public void heal(int amount) {
        health = Math.min(100, health + amount);
    }

    public void gainEnergy(int amount) {
        energy += amount;
    }

    public void spendEnergy(int amount) {
        energy = Math.max(0, energy - amount);
    }

    public void addActiveEffect(String effect) {
        activeEffects.add(effect);
    }

    public void removeActiveEffect(String effect) {
        activeEffects.remove(effect);
    }

    public boolean hasActiveEffect(String effect) {
        return activeEffects.contains(effect);
    }

    public int getHandSize() {
        return hand.size();
    }

    public int getPlayedCardsCount() {
        return playedCards.size();
    }

    public int getDiscardPileSize() {
        return discardPile.size();
    }

    public List<Card> getHand() {
        synchronized (lock) {
            return Collections.unmodifiableList(new ArrayList<>(hand));
        }
    }

    public List<Card> getPlayedCards() {
        synchronized (lock) {
            return Collections.unmodifiableList(new ArrayList<>(playedCards));
        }
    }

    public List<Card> getDiscardPile() {
        synchronized (lock) {
            return Collections.unmodifiableList(new ArrayList<>(discardPile));
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void reset() {
        health = 100;
        energy = 10;
        totalComplexity = 0;
        hasPlayedCardThisTurn = false;
        hand.clear();
        playedCards.clear();
        discardPile.clear();
        activeEffects.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name != null && name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format(
            "Player{name='%s', health=%d, energy=%d, handSize=%d, playedCards=%d}",
            name,
            health,
            energy,
            hand.size(),
            playedCards.size()
        );
    }
}
