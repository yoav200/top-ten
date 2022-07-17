package com.alhalel.topten.enteties;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerAchievements {

    private final int championships;

    private final int championshipsAba;

    private final int finalsMvp;

    private final int leagueMvp;

    private final int scoringChamp;

    private final int reboundChamp;

    private final int assistChamp;

    private final int stealsChamp;

    private final int blocksChamp;

    private final int defensivePlayerOfTheYear;

    private final int allNba;

    private final int allDefensive;

    private final int allStar;

    private final int allStarMvp;

    private final int rookieOfTheYear;

    private final int mostImprove;

}
