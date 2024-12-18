package com.programmingcardgame.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameGoal {
    private String description;
    private GoalType type;
    private int requiredComplexity;
    private boolean isCompetitive;

    public boolean isGoalAchieved(Player player) {
        // Placeholder för framtida implementering av måluppfyllelse
        return false;
    }

    public enum GoalType {
        COMPLEXITY_BASED,
        CARD_TYPE_BASED,
        FUNCTION_CREATION,
        RESOURCE_MANAGEMENT
    }
}