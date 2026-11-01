package com.eldoras.core;

/** Pure level progression formula, separated so it can be tested without a running server. */
public final class ExperienceCurve {
    private ExperienceCurve() {}

    public static long required(long base, double growth, int level) {
        if (base < 1) throw new IllegalArgumentException("base must be positive");
        if (!Double.isFinite(growth) || growth < 1.0) {
            throw new IllegalArgumentException("growth must be finite and at least 1.0");
        }
        if (level < 1) throw new IllegalArgumentException("level must be at least 1");
        double value = base * Math.pow(growth, level - 1);
        if (!Double.isFinite(value) || value >= Long.MAX_VALUE) return Long.MAX_VALUE;
        return Math.max(1L, (long) Math.floor(value));
    }
}
