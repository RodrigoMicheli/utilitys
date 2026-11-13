package com.utilitys.api.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitConverterUtilTest {

    @Test
    void shouldConvertKilometersToMeters() {
        BigDecimal result = UnitConverterUtil.convert("LENGTH", "KILOMETER", "METER", new BigDecimal("2.5"));
        assertEquals(0, new BigDecimal("2500").compareTo(result));
    }

    @Test
    void shouldConvertCelsiusToFahrenheit() {
        BigDecimal result = UnitConverterUtil.convert("TEMPERATURE", "CELSIUS", "FAHRENHEIT", new BigDecimal("100"));
        assertEquals(0, new BigDecimal("212").compareTo(result));
    }

    @Test
    void shouldConvertKilogramsToGrams() {
        BigDecimal result = UnitConverterUtil.convert("MASS", "KILOGRAM", "GRAM", new BigDecimal("1.75"));
        assertEquals(0, new BigDecimal("1750").compareTo(result));
    }

    @Test
    void shouldConvertGigabytesToMegabytes() {
        BigDecimal result = UnitConverterUtil.convert("DATA_STORAGE", "GB", "MB", new BigDecimal("2"));
        assertEquals(0, new BigDecimal("2048").compareTo(result));
    }
}
