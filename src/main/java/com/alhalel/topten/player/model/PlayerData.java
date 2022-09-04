package com.alhalel.topten.player.model;

import com.alhalel.topten.player.Player;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerData {
    private String uniqueName;
    private Integer playerReference;
    private String fullName;
    private String country;
    private String imageUrl;
    private String yearsActive;

    private String position;

    private String DOB;

    private String height;

    private String weight;
    private boolean active;
    private boolean eligibleForRanking;
    private double games;
    private double ptsPerGame;
    private double trbPerGme;
    private double astPerGame;
    private double fgPct;
    private double fg3Pct;
    private double ftPct;

    private String externalLink;

    private String externalLink2;

    public PlayerData(PlayerItem playerItem) {
        this.uniqueName = playerItem.getUniqueName();
        this.fullName = playerItem.getFullName();
        this.yearsActive = playerItem.getYearsActive();
        this.active = playerItem.isActive();
    }

    public PlayerData(Player player) {
        this.uniqueName = player.getUniqueName();
        this.playerReference = getPlayerReference();
        this.fullName = player.getPlayerInfo().getFullName();
        this.country = player.getPlayerInfo().getCountry();
        this.imageUrl = player.getPlayerInfo().getImageUrl();
        this.yearsActive = player.getPlayerInfo().getYearsActive();
        this.active = player.getPlayerInfo().isActive();
        this.position = player.getPlayerInfo().getPosition();
        this.DOB = player.getPlayerInfo().getDOB();
        this.height = player.getPlayerInfo().getHeight();
        this.weight = player.getPlayerInfo().getWeight();
        this.eligibleForRanking = player.isEligibleForRanking();
        this.games = player.getCareerPerGame().getGames();
        this.ptsPerGame = player.getCareerPerGame().getPtsPerGame();
        this.trbPerGme = player.getCareerPerGame().getTrbPerGme();
        this.astPerGame = player.getCareerPerGame().getAstPerGame();
        this.fgPct = player.getCareerPerGame().getFgPct();
        this.fg3Pct = player.getCareerPerGame().getFg3Pct();
        this.ftPct = player.getCareerPerGame().getFtPct();
    }

}
