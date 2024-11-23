package dev.jasper.game.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormatUtilsTest {

    @Test
    void testMax1For1DigitNumber() {
        Integer max = 1;
        Integer input = 3;
        String expected = "3";

        String actual = FormatUtils.formatToDigits(max, input);

        assertEquals(expected, actual);
    }
    @Test
    void testMax2For1DigitNumber() {
        Integer max = 2;
        Integer input = 3;
        String expected = "03";

        String actual = FormatUtils.formatToDigits(max, input);

        assertEquals(expected, actual);
    }
    @Test
    void testMax2For2DigitNumber() {
        Integer max = 2;
        Integer input = 45;
        String expected = "45";

        String actual = FormatUtils.formatToDigits(max, input);

        assertEquals(expected, actual);
    }
    @Test
    void testMax3For1DigitNumber() {
        Integer max = 3;
        Integer input = 3;
        String expected = "003";

        String actual = FormatUtils.formatToDigits(max, input);

        assertEquals(expected, actual);
    }
    @Test
    void testMax3For2DigitNumber() {
        Integer max = 3;
        Integer input = 95;
        String expected = "095";

        String actual = FormatUtils.formatToDigits(max, input);

        assertEquals(expected, actual);
    }
    @Test
    void testMax3For3DigitNumber() {
        Integer max = 3;
        Integer input = 123;
        String expected = "123";

        String actual = FormatUtils.formatToDigits(max, input);

        assertEquals(expected, actual);
    }
    @Test
    void testMax3For5DigitNumber() {
        Integer max = 3;
        Integer input = 12345;
        String expected = "345";

        String actual = FormatUtils.formatToDigits(max, input);

        assertEquals(expected, actual);
    }
}