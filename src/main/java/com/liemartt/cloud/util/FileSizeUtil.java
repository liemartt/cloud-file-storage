package com.liemartt.cloud.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FileSizeUtil {
    public static BigDecimal mapByteToMb(Long bytes) {
        if (bytes < 1024 * 1024) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(bytes / 1024. / 1024).setScale(2, RoundingMode.CEILING);
    }
    
    public static String getFileSizeWithMeasureUnit(Long bytes) {
        if (bytes / 1024 / 1024 > 0) {
            return bytes / 1024 / 1024 + " Mb";
        } else if (bytes / 1024 > 0) {
            return bytes / 1024 + " Kb";
        } else return bytes + "b";
    }
}
