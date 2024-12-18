package com.programmingcardgame.service;

import com.programmingcardgame.model.GameGoal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

    private final Random random = new Random();
    private final Map<GameGoal.GoalType, String> goalDescriptions =
        new HashMap<>();

    public GoalService() {
        initializeGoalDescriptions();
    }

    private void initializeGoalDescriptions() {
        goalDescriptions.put(
            GameGoal.GoalType.COMPLEXITY_BASED,
            "Nå en total komplexitet av %d"
        );
        goalDescriptions.put(
            GameGoal.GoalType.CARD_TYPE_BASED,
            "Spela %d kort av samma typ"
        );
        goalDescriptions.put(
            GameGoal.GoalType.FUNCTION_CREATION,
            "Skapa en funktion med minst %d parametrar"
        );
        goalDescriptions.put(
            GameGoal.GoalType.RESOURCE_MANAGEMENT,
            "Samla %d energipoäng"
        );
    }

    public GameGoal generateRandomGoal() {
        GameGoal.GoalType type = getRandomGoalType();
        int complexity = generateComplexity();
        return GameGoal.builder()
            .type(type)
            .description(String.format(goalDescriptions.get(type), complexity))
            .requiredComplexity(complexity)
            .isCompetitive(random.nextBoolean())
            .build();
    }

    private GameGoal.GoalType getRandomGoalType() {
        GameGoal.GoalType[] types = GameGoal.GoalType.values();
        return types[random.nextInt(types.length)];
    }

    private int generateComplexity() {
        return random.nextInt(3, 8); // Mer utmanande komplexitetsnivåer
    }
}
