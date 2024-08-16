package com.tinqinacademy.authentication.persistence.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("admin"),
    USER("user"),
    UNKNOWN("");

    private final String code;

    RoleType(String code) {
        this.code = code;
    }

    @JsonCreator
    public static RoleType getByCode(String input) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.toString().equals(input)) {
                return roleType;
            }
        }
        return RoleType.UNKNOWN;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.code;
    }
}
