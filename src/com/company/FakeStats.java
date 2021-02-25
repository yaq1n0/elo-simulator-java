package com.company;

import java.util.Random;

public class FakeStats {
    public static double getSimulatedBiasedRandomness(boolean allowNegative) {
        Random random = new Random();
        double randomDouble = random.nextDouble();

        double value;

        if (randomDouble > 0.682) {
            if (randomDouble > 0.954) {
                if (randomDouble > 0.996) {
                    value = 0.60;
                } else {
                    value = 0.40;
                }
            } else {
                value = 0.25;
            }
        } else {
            value = 0.10;
        }

        if (allowNegative) {
            if (random.nextBoolean()) {
                return value;
            } else {
                return value * -1.0;
            }
        } else {
            return value;
        }
    }
}
