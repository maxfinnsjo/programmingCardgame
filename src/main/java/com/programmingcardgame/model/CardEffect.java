package com.programmingcardgame.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardEffect {

    private EffectType type;
    private int value;
    private String target;

    public enum EffectType {
        DAMAGE,
        HEAL,
        DRAW_CARD,
        GAIN_ENERGY,
        MODIFY_COMPLEXITY,
        COPY_CARD,
        TRANSFORM_CARD,
    }

    // Add no-args constructor
    public CardEffect() {}
}
