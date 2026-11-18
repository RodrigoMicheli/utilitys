package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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

        BigDecimal kelvin = UnitConverterUtil.convert("TEMPERATURE", "°C", "K", BigDecimal.ZERO);
        assertEquals(0, new BigDecimal("273.15").compareTo(kelvin));
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

    @Test
    void shouldConvertVolumeAndSpeed() {
        BigDecimal ml = UnitConverterUtil.convert("VOLUME", "l", "ml", new BigDecimal("1.5"));
        assertEquals(0, new BigDecimal("1500").compareTo(ml));

        BigDecimal ms = UnitConverterUtil.convert("SPEED", "km/h", "m/s", new BigDecimal("36"));
        assertEquals(0, new BigDecimal("10").compareTo(ms));
    }

    @Test
    void shouldRejectInvalidCategoryOrUnit() {
        assertThrows(UtilityException.class, () -> UnitConverterUtil.convert("INVALID", "m", "km", BigDecimal.ONE));
        assertThrows(UtilityException.class, () -> UnitConverterUtil.convert("LENGTH", "invalid", "km", BigDecimal.ONE));
        assertThrows(UtilityException.class, () -> UnitConverterUtil.convert("TEMPERATURE", null, "C", BigDecimal.ONE));
    }
}
