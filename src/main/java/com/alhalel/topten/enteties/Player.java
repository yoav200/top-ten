package com.alhalel.topten.enteties;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Player {

    private final Long id;

    private final PlayerInfo playerInfo;

    private final PlayerStats careerPerGame;

    private final PlayerStats playoffsPerGame;

    private final PlayerAchievements achievements;

}
