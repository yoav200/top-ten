package com.alhalel.topten.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerStatisticsModel {

    private final Integer rankLow;

    private final Integer rankHigh;

    private final Double rankAverage;

    public boolean isValid() {
        return rankLow != null && rankHigh != null && rankAverage != null;
    }
}
