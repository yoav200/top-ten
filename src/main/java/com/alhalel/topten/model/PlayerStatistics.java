package com.alhalel.topten.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerStatistics {

    private final int rankLow;

    private final int rankHigh;

    private final double rankAverage;

}
