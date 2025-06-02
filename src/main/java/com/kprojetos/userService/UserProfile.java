package com.kprojetos.userService;

import lombok.Getter;

public enum UserProfile {
    ADMINISTRATOR("administrator"),
    COMMON("common");

    @Getter
    private final String type;

    UserProfile(String type) {
        this.type = type;
    }
}
