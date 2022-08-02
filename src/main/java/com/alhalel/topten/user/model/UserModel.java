package com.alhalel.topten.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserModel {
    private String name;
    private String imageUrl;

}
