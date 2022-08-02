package com.alhalel.topten.player.model;

import com.alhalel.topten.player.Player;
import com.alhalel.topten.util.ResourceUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerData {
    private String uniqueName;

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

    public PlayerData(Player player) {
        this.uniqueName = player.getUniqueName();
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
