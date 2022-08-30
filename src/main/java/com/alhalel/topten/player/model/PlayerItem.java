package com.alhalel.topten.player.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerItem {

    private String uniqueName;

    private Integer playerId;
    private String fullName;
    private String yearsActive;

    private boolean active;

    public String getId() {
        return uniqueName;
    }

    public String getText() {
        return fullName + " " + yearsActive;
    }

    public String getSlug() {
        return uniqueName;
    }
}
