package com.hashedin.hashedinbank.logic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class CreditCardNumberGeneratorLogic {
    private final Random random = new Random(System.currentTimeMillis());

    /**
     * @param bin    The bank identification number, a set digits at the start of the credit card
     *               number, used to identify the bank that is issuing the credit card.
     * @param length The total length (i.e. including the BIN) of the credit card number.
     * @return A randomly generated, valid, credit card number.
     */
    public String generate(String bin, int length) {

        int randomNumberLength = length - (bin.length() + 1);

        final StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = this.random.nextInt(10);
            builder.append(digit);
        }

        int checkDigit = this.getCheckDigit(builder.toString());
        builder.append(checkDigit);
        return builder.toString();
    }

    public String generateCvvNumber(String ccNumber) {
        List<Character> characters = new ArrayList<>();
        characters.add(ccNumber.charAt(3));
        characters.add(ccNumber.charAt(7));
        characters.add(ccNumber.charAt(11));
        Collections.shuffle(characters);
        return StringUtils.join(characters.toArray());
    }

    /**
     * Generates the check digit required to make the given credit card number
     * valid (i.e. pass the Luhn check)
     *
     * @param number The credit card number for which to generate the check digit.
     * @return The check digit required to make the given credit card number
     * valid.
     */
    private int getCheckDigit(String number) {

        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            // Get the digit at the current position.
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }
}