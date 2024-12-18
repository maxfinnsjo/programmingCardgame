package com.programmingcardgame.service;

import com.programmingcardgame.model.Card;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CombinationService {

    public boolean validateCombination(List<Card> cards) {
        // Validera om korten kan kombineras
        return true;
    }

    public int calculateCombinationValue(List<Card> cards) {
        return cards.stream().mapToInt(card -> card.getComplexity()).sum();
    }
}
