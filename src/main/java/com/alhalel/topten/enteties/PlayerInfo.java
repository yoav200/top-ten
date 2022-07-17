package com.alhalel.topten.enteties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerInfo {
    private final String fullName;

    private final String uniqueName;

    private final String country;

    private final String imageUrl;

    private final String position;

    private final String DOB;

    private final String height;

    private final String wight;

    private final String draft;

    private final String NBADebut;

    private final String yearsActive;

}
