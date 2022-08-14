package com.alhalel.topten.scrapers;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerData {

    private int personId;
    private String playerLastName;
    private String playerFirstName;
    private String playerSlug;
    private Integer teamId;
    private Integer teamSlug;
    private String isDefunct;
    private String teamCity;
    private String teamName;
    private String teamAbbreviation;
    private Integer jerseyNumber;
    private String position;
    private String height;
    private String weight;
    private String college;
    private String country;
    private Integer draftYear;
    private Integer draftRound;
    private Integer draftNumber;
    private Double rosterStatus;
    private Double pts;
    private Double reb;
    private Double ast;
    private String statsTimeframe;
    private String fromYear;
    private String toYear;

}
