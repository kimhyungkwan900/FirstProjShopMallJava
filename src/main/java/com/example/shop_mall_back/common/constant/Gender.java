package com.example.shop_mall_back.common.constant;

public enum Gender {
    MALE,FEMALE,UNKNOWN;

    public static Gender conversion(String value) {
        if (value == null) return UNKNOWN;
        return switch (value.toUpperCase()) {
            case "M" -> MALE;
            case "F" -> FEMALE;
            default -> UNKNOWN;
        };
    }
}
