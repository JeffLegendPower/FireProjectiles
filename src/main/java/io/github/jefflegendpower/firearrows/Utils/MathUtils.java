package io.github.jefflegendpower.firearrows.Utils;

import java.util.List;

public class MathUtils {

    // finds the nearest number to the value from a given list of numbers
    public static int closest(int comparedNumber, List<Integer> integerList) {
        int min = Integer.MAX_VALUE;
        int closest = comparedNumber;

        for (int integer : integerList) {
            final int diff = Math.abs(integer - comparedNumber);

            if (diff < min) {
                min = diff;
                closest = integer;
            }
        }

        return closest;
    }
}
