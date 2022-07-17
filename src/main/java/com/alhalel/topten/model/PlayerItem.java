package com.alhalel.topten.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerItem {

    private String uniqueName;
    private String fullName;
    private String yearsActive;

    private String imageUrl;

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
