package com.hashedin.hashedinbank.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public final class MaskCCNumberLogic {

    private MaskCCNumberLogic() {
    }

    public static String maskStringField(final char maskValue, final String currValue) {
        if (maskValue != ' ') {
            String part1 = currValue.substring(0, 4);
            String part2 = currValue.substring(4, currValue.length() - 4).replaceAll("\\d",
                    String.valueOf(maskValue));

            String part3 = currValue.substring(currValue.length() - 4);

            return part1 + part2 + part3;
        }
        return currValue;
    }
}
