package dev.jasper.game.tools;

public class FormatUtils {
    private FormatUtils() {}
    public static String formatToDigits(Integer maxDigits, Integer number) {
        // Demo:
        // 3, 5 -> 005
        // 3, 99 -> 099
        // 2, 5 -> 05
        // 2, 232 -> 32
        // Steps:
        // 1. split into String[]
        // 2. If length < maxDigits, shift 0's to head, join
        // 3. If length = maxDigits, join
        // 4. If length > maxDigits, pop the ones that are not needed, join
//        number.toString().split('');

        int lengthOfDigits = number.toString().length();
        int zerosNeeded = maxDigits - lengthOfDigits;

        String results;
        if (lengthOfDigits < maxDigits) {
            String toConcat = "";
            for (int i = 0; i < zerosNeeded; i++) {
                toConcat = toConcat.concat("0");
            }
            results = toConcat.concat(number.toString());

        } else {
            results = number.toString().substring(lengthOfDigits - maxDigits);
        }

        return results;
    }
}
