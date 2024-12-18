package com.programmingcardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private String name;
    private CardType type;
    private String code;
    private int complexity;
    private int energyCost;
    private String description;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private List<CardEffect> effects = new ArrayList<>();

    @Builder.Default
    private List<String> requirements = new ArrayList<>();

    @Builder.Default
    private Set<CardType> synergies = new HashSet<>();

    // Tillfälliga modifierare som kan påverka kortet under en tur
    private int temporaryComplexityModifier;
    private int temporaryEnergyCostModifier;

    // Fluent API methods (returnar this för kedjning)
    public Card addEffect(CardEffect effect) {
        this.effects.add(effect);
        return this;
    }

    public Card addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public Card addRequirement(String requirement) {
        this.requirements.add(requirement);
        return this;
    }

    public Card addSynergy(CardType cardType) {
        this.synergies.add(cardType);
        return this;
    }

    // Resten av metoderna...
    public int calculateSynergyValue(Card otherCard) {
        int synergyValue = 0;

        if (synergies.contains(otherCard.getType())) {
            synergyValue += 2;
        }

        long commonTags = tags
            .stream()
            .filter(tag -> otherCard.getTags().contains(tag))
            .count();
        synergyValue += commonTags;

        if (Math.abs(complexity - otherCard.getComplexity()) <= 1) {
            synergyValue += 1;
        }

        return synergyValue;
    }

    public boolean canBeCombinedWith(Card other) {
        if (hasConflictingEffects(other)) {
            return false;
        }

        return meetsRequirements(other) && other.meetsRequirements(this);
    }

    private boolean hasConflictingEffects(Card other) {
        return false; // Placeholder
    }

    private boolean meetsRequirements(Card other) {
        return other
            .getRequirements()
            .stream()
            .allMatch(
                req ->
                    this.tags.contains(req) || this.type.toString().equals(req)
            );
    }

    public int getEffectiveComplexity() {
        return complexity + temporaryComplexityModifier;
    }

    public int getEffectiveEnergyCost() {
        return Math.max(0, energyCost + temporaryEnergyCostModifier);
    }

    public List<CardEffect> getEffects() {
        return Collections.unmodifiableList(effects);
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public List<String> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    public Set<CardType> getSynergies() {
        return Collections.unmodifiableSet(synergies);
    }

    public void applyTemporaryModifiers(int complexityMod, int energyCostMod) {
        this.temporaryComplexityModifier = complexityMod;
        this.temporaryEnergyCostModifier = energyCostMod;
    }

    public void clearTemporaryModifiers() {
        this.temporaryComplexityModifier = 0;
        this.temporaryEnergyCostModifier = 0;
    }

    public boolean isPlayable(Player player) {
        return (
            player.getEnergy() >= getEffectiveEnergyCost() &&
            requirements
                .stream()
                .allMatch(req ->
                    player
                        .getPlayedCards()
                        .stream()
                        .anyMatch(
                            c ->
                                c.getTags().contains(req) ||
                                c.getType().toString().equals(req)
                        )
                )
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return name != null && name.equals(card.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format(
            "Card{name='%s', type=%s, complexity=%d, energyCost=%d}",
            name,
            type,
            complexity,
            energyCost
        );
    }
}
