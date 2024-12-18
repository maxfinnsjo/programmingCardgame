package com.programmingcardgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}
