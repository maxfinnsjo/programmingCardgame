package com.programmingcardgame.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameGoal {

    private GoalType type;
    private String description;
    private int requiredComplexity;
    private boolean isCompetitive;

    public enum GoalType {
        COMPLEXITY_BASED,
        CARD_TYPE_BASED,
        FUNCTION_CREATION,
        RESOURCE_MANAGEMENT,
    }
}
