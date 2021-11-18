package io.github.erp.internal.model.mapping;

import io.github.erp.domain.enumeration.CategoryTypes;

public class PaymentCategoryMappingUtility {

    public static CategoryTypes categoryStringToCategoryType(String categoryString) {
        switch (categoryString) {
            case "CATEGORY1": {
                return CategoryTypes.CATEGORY1;
            }

            case "CATEGORY2": {
                return CategoryTypes.CATEGORY2;
            }

            case "CATEGORY3": {
                return CategoryTypes.CATEGORY3;
            }

            case "CATEGORY4": {
                return CategoryTypes.CATEGORY4;
            }

            case "CATEGORY5": {
                return CategoryTypes.CATEGORY5;
            }
            case "CATEGORY6": {
                return CategoryTypes.CATEGORY6;
            }

            case "CATEGORY7": {
                return CategoryTypes.CATEGORY7;
            }

            case "CATEGORY8": {
                return CategoryTypes.CATEGORY8;
            }

            case "CATEGORY9": {
                return CategoryTypes.CATEGORY9;
            }

            case "CATEGORY10": {
                return CategoryTypes.CATEGORY10;
            }

            case "CATEGORY11": {
                return CategoryTypes.CATEGORY11;
            }

            case "CATEGORY12": {
                return CategoryTypes.CATEGORY12;
            }

            case "CATEGORY13": {
                return CategoryTypes.CATEGORY13;
            }

            case "CATEGORY14": {
                return CategoryTypes.CATEGORY14;
            }

            case "CATEGORY15": {
                return CategoryTypes.CATEGORY15;
            }

            case "CATEGORY16": {
                return CategoryTypes.CATEGORY16;
            }

            case "CATEGORY17": {
                return CategoryTypes.CATEGORY17;
            }

            case "UNDEFINED": {
                return CategoryTypes.UNDEFINED;
            }
            default: {
                return CategoryTypes.CATEGORY0;
            }
        }
    }

    public static String categoryTypeToCategoryString(CategoryTypes categoryType) {
        switch (categoryType) {
            case CATEGORY1: {
                return "CATEGORY1";
            }
            case CATEGORY2: {
                return "CATEGORY2";
            }
            case CATEGORY3: {
                return "CATEGORY3";
            }
            case CATEGORY4: {
                return "CATEGORY4";
            }
            case CATEGORY5: {
                return "CATEGORY5";
            }
            case CATEGORY6: {
                return "CATEGORY6";
            }
            case CATEGORY7: {
                return "CATEGORY7";
            }
            case CATEGORY8: {
                return "CATEGORY8";
            }
            case CATEGORY9: {
                return "CATEGORY9";
            }
            case CATEGORY10: {
                return "CATEGORY10";
            }
            case CATEGORY11: {
                return "CATEGORY11";
            }
            case CATEGORY12: {
                return "CATEGORY12";
            }
            case CATEGORY13: {
                return "CATEGORY14";
            }
            case CATEGORY15: {
                return "CATEGORY15";
            }
            case CATEGORY16: {
                return "CATEGORY16";
            }
            case CATEGORY17: {
                return "CATEGORY17";
            }
            case UNDEFINED: {
                return "UNDEFINED";
            }
            default: {
                return "CATEGORY0";
            }
        }
    }
}
