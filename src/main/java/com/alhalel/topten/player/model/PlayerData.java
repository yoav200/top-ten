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

    private boolean active;

    private boolean eligibleForSaving;

    private double games;

    private double ptsPerGame;

    private double trbPerGme;

    private double astPerGame;

    private double fgPct;

    private double fg3Pct;

    private double ftPct;

    private String externalLink;

    private String externalLink2;

    public PlayerData(Player player) {
        this.uniqueName = player.getUniqueName();
        this.playerReference = getPlayerReference();
        this.fullName = player.getPlayerInfo().getFullName();
        this.country = player.getPlayerInfo().getCountry();
        this.imageUrl = player.getPlayerInfo().getImageUrl();
        this.yearsActive = player.getPlayerInfo().getYearsActive();
        this.active = player.getPlayerInfo().isActive();
        this.eligibleForSaving = player.isEligibleForSaving();
        this.games = player.getCareerPerGame().getGames();
        this.ptsPerGame = player.getCareerPerGame().getPtsPerGame();
        this.trbPerGme = player.getCareerPerGame().getTrbPerGme();
        this.astPerGame = player.getCareerPerGame().getAstPerGame();
        this.fgPct = player.getCareerPerGame().getFgPct();
        this.fg3Pct = player.getCareerPerGame().getFg3Pct();
        this.ftPct = player.getCareerPerGame().getFtPct();
    }

}
