package com.eldoras.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExperienceCurveTest {
    @Test void firstLevelUsesBaseRequirement() {
        assertEquals(100L, ExperienceCurve.required(100, 1.18, 1));
    }

    @Test void requirementGrowsByConfiguredRate() {
        assertEquals(118L, ExperienceCurve.required(100, 1.18, 2));
    }

    @Test void veryHighLevelsSaturateInsteadOfOverflowing() {
        assertEquals(Long.MAX_VALUE, ExperienceCurve.required(100, 2.0, 10000));
    }

    @Test void rejectsInvalidConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> ExperienceCurve.required(0, 1.1, 1));
        assertThrows(IllegalArgumentException.class, () -> ExperienceCurve.required(100, 0.9, 1));
        assertThrows(IllegalArgumentException.class, () -> ExperienceCurve.required(100, 1.1, 0));
    }
}
