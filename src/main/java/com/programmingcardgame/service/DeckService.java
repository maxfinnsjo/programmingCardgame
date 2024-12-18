package com.programmingcardgame.service;

import com.programmingcardgame.model.Card;
import com.programmingcardgame.model.CardEffect;
import com.programmingcardgame.model.CardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeckService {

    private static final Logger logger = LoggerFactory.getLogger(
        DeckService.class
    );
    private List<Card> deck;

    public DeckService() {
        initializeDeck();
    }

    private void initializeDeck() {
        deck = new ArrayList<>();

        // Lägg till kort per kategori
        addControlStructures();
        addFunctions();
        addVariables();
        addOperators();
        addBuiltInFunctions();

        logger.info("Deck initialized with {} cards", deck.size());
    }

    private void addControlStructures() {
        deck.add(
            Card.builder()
                .name("If Statement")
                .type(CardType.CONTROL_STRUCTURE)
                .code("if (condition) { }")
                .complexity(2)
                .energyCost(2)
                .description("Conditional execution")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            1,
                            "combination"
                        ),
                        new CardEffect(
                            CardEffect.EffectType.DRAW_CARD,
                            1,
                            "self"
                        )
                    )
                )
                .build()
        );

        deck.add(
            Card.builder()
                .name("While Loop")
                .type(CardType.CONTROL_STRUCTURE)
                .code("while (condition) { }")
                .complexity(2)
                .energyCost(2)
                .description("Loop while condition is true")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            2,
                            "combination"
                        )
                    )
                )
                .build()
        );

        deck.add(
            Card.builder()
                .name("For Loop")
                .type(CardType.CONTROL_STRUCTURE)
                .code("for(int i=0; i<n; i++)")
                .complexity(2)
                .energyCost(3)
                .description("Iterate n times")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            2,
                            "combination"
                        ),
                        new CardEffect(
                            CardEffect.EffectType.GAIN_ENERGY,
                            1,
                            "self"
                        )
                    )
                )
                .build()
        );

        deck.add(
            Card.builder()
                .name("Try-Catch")
                .type(CardType.CONTROL_STRUCTURE)
                .code("try { } catch (Exception e) { }")
                .complexity(3)
                .energyCost(3)
                .description("Error handling")
                .effects(
                    Arrays.asList(
                        new CardEffect(CardEffect.EffectType.HEAL, 2, "self"),
                        new CardEffect(
                            CardEffect.EffectType.GAIN_ENERGY,
                            1,
                            "self"
                        )
                    )
                )
                .build()
        );
    }

    private void addFunctions() {
        deck.add(
            Card.builder()
                .name("Print Function")
                .type(CardType.FUNCTION)
                .code("System.out.println()")
                .complexity(1)
                .energyCost(1)
                .description("Output text to console")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.DAMAGE,
                            1,
                            "opponent"
                        )
                    )
                )
                .build()
        );

        deck.add(
            Card.builder()
                .name("Custom Function")
                .type(CardType.FUNCTION)
                .code("void customFunction() { }")
                .complexity(2)
                .energyCost(2)
                .description("Create a custom function")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            2,
                            "combination"
                        ),
                        new CardEffect(
                            CardEffect.EffectType.DRAW_CARD,
                            1,
                            "self"
                        )
                    )
                )
                .build()
        );
    }

    private void addVariables() {
        deck.add(
            Card.builder()
                .name("Integer Variable")
                .type(CardType.VARIABLE)
                .code("int number = 0;")
                .complexity(1)
                .energyCost(1)
                .description("Create an integer variable")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            1,
                            "combination"
                        )
                    )
                )
                .build()
        );

        deck.add(
            Card.builder()
                .name("Array Declaration")
                .type(CardType.VARIABLE)
                .code("int[] array = new int[10];")
                .complexity(2)
                .energyCost(2)
                .description("Create a new array")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.DRAW_CARD,
                            1,
                            "self"
                        ),
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            1,
                            "combination"
                        )
                    )
                )
                .build()
        );
    }

    private void addOperators() {
        deck.add(
            Card.builder()
                .name("Addition")
                .type(CardType.OPERATOR)
                .code("+")
                .complexity(1)
                .energyCost(1)
                .description("Add two values")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            1,
                            "combination"
                        )
                    )
                )
                .build()
        );

        deck.add(
            Card.builder()
                .name("Multiplication")
                .type(CardType.OPERATOR)
                .code("*")
                .complexity(1)
                .energyCost(2)
                .description("Multiply two values")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            2,
                            "combination"
                        ),
                        new CardEffect(
                            CardEffect.EffectType.DAMAGE,
                            1,
                            "opponent"
                        )
                    )
                )
                .build()
        );
    }

    private void addBuiltInFunctions() {
        deck.add(
            Card.builder()
                .name("Math.random()")
                .type(CardType.BUILT_IN_FUNCTION)
                .code("Math.random()")
                .complexity(1)
                .energyCost(2)
                .description("Generate random number")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.DRAW_CARD,
                            1,
                            "self"
                        )
                    )
                )
                .build()
        );

        deck.add(
            Card.builder()
                .name("String.length()")
                .type(CardType.BUILT_IN_FUNCTION)
                .code("string.length()")
                .complexity(1)
                .energyCost(1)
                .description("Get string length")
                .effects(
                    Arrays.asList(
                        new CardEffect(
                            CardEffect.EffectType.MODIFY_COMPLEXITY,
                            1,
                            "combination"
                        ),
                        new CardEffect(
                            CardEffect.EffectType.GAIN_ENERGY,
                            1,
                            "self"
                        )
                    )
                )
                .build()
        );
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
        logger.debug("Deck shuffled");
    }

    public Card drawCard() {
        if (deck.isEmpty()) {
            logger.info("Deck empty, reinitializing and shuffling");
            initializeDeck();
            shuffleDeck();
        }
        Card drawnCard = deck.remove(0);
        logger.debug("Card drawn: {}", drawnCard.getName());
        return drawnCard;
    }

    public List<Card> getDeck() {
        return new ArrayList<>(deck);
    }

    public int getRemainingCards() {
        return deck.size();
    }
}
