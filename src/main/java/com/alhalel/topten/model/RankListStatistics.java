package com.alhalel.topten.model;

import com.alhalel.topten.enteties.PlayerAchievements;
import com.alhalel.topten.enteties.PlayerStats;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

public class RankListStatistics {

    private PlayerAchievements achievements;

    private Set<PlayerStats> playerStats;
}
