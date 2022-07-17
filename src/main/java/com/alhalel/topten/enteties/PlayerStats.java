package com.alhalel.topten.enteties;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerStats {

    private final double games;

    private final double ptsPerGame;

    private final double trbPerGme;

    private final double astPerGame;

    private final double stlPerGame;

    private final double blkPerGame;

    private final double tovPerGame;

    private final double fgPct;

    private final double fg3Pct;

    private final double ftPct;

    private final double efgPct;

}
