package com.alhalel.topten.scrapers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerData {
    private final int personId;
    private final String playerLastName;
    private final String playerFirstName;
    private final String playerSlug;
    private final Integer teamId;
    private final Integer teamSlug;
    private final String isDefunct;
    private final String teamCity;
    private final String teamName;
    private final String teamAbbreviation;
    private final Integer jerseyNumber;
    private final String position;
    private final String height;
    private final String weight;
    private final String college;
    private final String country;
    private final Integer draftYear;
    private final Integer draftRound;
    private final Integer draftNumber;
    private final Double rosterStatus;
    private final Double pts;
    private final Double reb;
    private final Double ast;
    private final String statsTimeframe;
    private final String fromYear;
    private final String toYear;

}
