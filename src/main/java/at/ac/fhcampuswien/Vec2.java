package at.ac.fhcampuswien;

import lombok.Value;

import java.util.concurrent.ThreadLocalRandom;

@Value
public class Vec2 {
    int col;
    int row;

    static Vec2 getRandom(int minCol, int maxCol, int minRow, int maxRow) {
        return new Vec2(getRandom(minCol, maxCol), getRandom(minRow, maxRow));
    }

    private static int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
