package ru.trelloiii.lib.api.defined;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InRangeDefinedValidator {

    public static boolean validateLong(Long value, long[] range) {
        for (long rangeValue : range) {
            if (Long.valueOf(rangeValue).equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateInt(Integer value, int[] range) {
        for (int rangeValue : range) {
            if (Integer.valueOf(rangeValue).equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateString(String value, String[] range) {
        for (String rangeValue : range) {
            if (rangeValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
