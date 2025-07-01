package com.example.shop_mall_back.common.constant;

public enum Age {
    AGE_10,AGE_20,AGE_30,AGE_40,AGE_50,AGE_60,AGE_70,AGE_80,AGE_90,UNKNOWN;

    public static Age conversion(String value) {
        if (value == null) return UNKNOWN;

        return switch (value) {
            case "10-19" -> AGE_10;
            case "20-29" -> AGE_20;
            case "30-39" -> AGE_30;
            case "40-49" -> AGE_40;
            case "50-59" -> AGE_50;
            case "60-69" -> AGE_60;
            case "70-79" -> AGE_70;
            case "80-89" -> AGE_80;
            case "90-99" -> AGE_90;
            default -> UNKNOWN;
        };
    }
}
