package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitária estática para conversão de unidades de medida:
 * Comprimento (Length), Massa/Peso (Mass), Temperatura (Temperature),
 * Volume, Velocidade (Speed) e Armazenamento de Dados (DataStorage).
 */
public final class UnitConverterUtil {

    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);
    private static final int DEFAULT_SCALE = 6;

    private UnitConverterUtil() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }

    public enum Category {
        LENGTH("Comprimento"),
        MASS("Massa / Peso"),
        TEMPERATURE("Temperatura"),
        VOLUME("Volume"),
        SPEED("Velocidade"),
        DATA_STORAGE("Armazenamento de Dados");

        private final String description;

        Category(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum LengthUnit {
        MILLIMETER("mm", new BigDecimal("0.001")),
        CENTIMETER("cm", new BigDecimal("0.01")),
        METER("m", BigDecimal.ONE),
        KILOMETER("km", new BigDecimal("1000")),
        INCH("in", new BigDecimal("0.0254")),
        FOOT("ft", new BigDecimal("0.3048")),
        YARD("yd", new BigDecimal("0.9144")),
        MILE("mi", new BigDecimal("1609.344"));

        private final String symbol;
        private final BigDecimal toMetersFactor;

        LengthUnit(String symbol, BigDecimal toMetersFactor) {
            this.symbol = symbol;
            this.toMetersFactor = toMetersFactor;
        }

        public String getSymbol() {
            return symbol;
        }

        public BigDecimal getToMetersFactor() {
            return toMetersFactor;
        }

        public static LengthUnit fromString(String value) {
            if (value == null || value.isBlank()) {
                throw new UtilityException("Unidade de comprimento obrigatória.");
            }
            String normalized = value.trim();
            for (LengthUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(normalized) || unit.symbol.equalsIgnoreCase(normalized)) {
                    return unit;
                }
            }
            throw new UtilityException("Unidade de comprimento inválida: " + value);
        }
    }

    public enum MassUnit {
        MILLIGRAM("mg", new BigDecimal("0.000001")),
        GRAM("g", new BigDecimal("0.001")),
        KILOGRAM("kg", BigDecimal.ONE),
        TON("t", new BigDecimal("1000")),
        OUNCE("oz", new BigDecimal("0.028349523125")),
        POUND("lb", new BigDecimal("0.45359237"));

        private final String symbol;
        private final BigDecimal toKilogramsFactor;

        MassUnit(String symbol, BigDecimal toKilogramsFactor) {
            this.symbol = symbol;
            this.toKilogramsFactor = toKilogramsFactor;
        }

        public String getSymbol() {
            return symbol;
        }

        public BigDecimal getToKilogramsFactor() {
            return toKilogramsFactor;
        }

        public static MassUnit fromString(String value) {
            if (value == null || value.isBlank()) {
                throw new UtilityException("Unidade de massa obrigatória.");
            }
            String normalized = value.trim();
            for (MassUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(normalized) || unit.symbol.equalsIgnoreCase(normalized)) {
                    return unit;
                }
            }
            throw new UtilityException("Unidade de massa inválida: " + value);
        }
    }

    public enum TemperatureUnit {
        CELSIUS("C"),
        FAHRENHEIT("F"),
        KELVIN("K");

        private final String symbol;

        TemperatureUnit(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public static TemperatureUnit fromString(String value) {
            if (value == null || value.isBlank()) {
                throw new UtilityException("Unidade de temperatura obrigatória.");
            }
            String normalized = value.replace("°", "").trim();
            for (TemperatureUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(normalized) || unit.symbol.equalsIgnoreCase(normalized)) {
                    return unit;
                }
            }
            throw new UtilityException("Unidade de temperatura inválida: " + value);
        }
    }

    public enum VolumeUnit {
        MILLILITER("ml", new BigDecimal("0.001")),
        LITER("l", BigDecimal.ONE),
        CUBIC_METER("m3", new BigDecimal("1000")),
        GALLON_US("gal", new BigDecimal("3.785411784")),
        FLUID_OUNCE_US("fl_oz", new BigDecimal("0.0295735295625"));

        private final String symbol;
        private final BigDecimal toLitersFactor;

        VolumeUnit(String symbol, BigDecimal toLitersFactor) {
            this.symbol = symbol;
            this.toLitersFactor = toLitersFactor;
        }

        public String getSymbol() {
            return symbol;
        }

        public BigDecimal getToLitersFactor() {
            return toLitersFactor;
        }

        public static VolumeUnit fromString(String value) {
            if (value == null || value.isBlank()) {
                throw new UtilityException("Unidade de volume obrigatória.");
            }
            String normalized = value.trim();
            for (VolumeUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(normalized) || unit.symbol.equalsIgnoreCase(normalized)) {
                    return unit;
                }
            }
            throw new UtilityException("Unidade de volume inválida: " + value);
        }
    }

    public enum SpeedUnit {
        MS("m/s", BigDecimal.ONE),
        KMH("km/h", new BigDecimal("0.2777777777777778")),
        MPH("mph", new BigDecimal("0.44704")),
        KNOT("kn", new BigDecimal("0.5144444444444444"));

        private final String symbol;
        private final BigDecimal toMetersPerSecondFactor;

        SpeedUnit(String symbol, BigDecimal toMetersPerSecondFactor) {
            this.symbol = symbol;
            this.toMetersPerSecondFactor = toMetersPerSecondFactor;
        }

        public String getSymbol() {
            return symbol;
        }

        public BigDecimal getToMetersPerSecondFactor() {
            return toMetersPerSecondFactor;
        }

        public static SpeedUnit fromString(String value) {
            if (value == null || value.isBlank()) {
                throw new UtilityException("Unidade de velocidade obrigatória.");
            }
            String normalized = value.trim();
            for (SpeedUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(normalized) || unit.symbol.equalsIgnoreCase(normalized)) {
                    return unit;
                }
            }
            throw new UtilityException("Unidade de velocidade inválida: " + value);
        }
    }

    public enum DataStorageUnit {
        BYTE("B", BigDecimal.ONE),
        KILOBYTE("KB", new BigDecimal("1024")),
        MEGABYTE("MB", new BigDecimal("1048576")),
        GIGABYTE("GB", new BigDecimal("1073741824")),
        TERABYTE("TB", new BigDecimal("1099511627776"));

        private final String symbol;
        private final BigDecimal toBytesFactor;

        DataStorageUnit(String symbol, BigDecimal toBytesFactor) {
            this.symbol = symbol;
            this.toBytesFactor = toBytesFactor;
        }

        public String getSymbol() {
            return symbol;
        }

        public BigDecimal getToBytesFactor() {
            return toBytesFactor;
        }

        public static DataStorageUnit fromString(String value) {
            if (value == null || value.isBlank()) {
                throw new UtilityException("Unidade de armazenamento obrigatória.");
            }
            String normalized = value.trim();
            for (DataStorageUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(normalized) || unit.symbol.equalsIgnoreCase(normalized)) {
                    return unit;
                }
            }
            throw new UtilityException("Unidade de armazenamento inválida: " + value);
        }
    }

    public static BigDecimal convertLength(BigDecimal value, LengthUnit from, LengthUnit to) {
        validateNotNull(value, from, to);
        BigDecimal inMeters = value.multiply(from.getToMetersFactor(), MC);
        return normalize(inMeters.divide(to.getToMetersFactor(), MC));
    }

    public static BigDecimal convertMass(BigDecimal value, MassUnit from, MassUnit to) {
        validateNotNull(value, from, to);
        BigDecimal inKilograms = value.multiply(from.getToKilogramsFactor(), MC);
        return normalize(inKilograms.divide(to.getToKilogramsFactor(), MC));
    }

    public static BigDecimal convertTemperature(BigDecimal value, TemperatureUnit from, TemperatureUnit to) {
        validateNotNull(value, from, to);
        if (from == to) {
            return normalize(value);
        }
        BigDecimal celsius = switch (from) {
            case CELSIUS -> value;
            case FAHRENHEIT -> value.subtract(new BigDecimal("32"), MC)
                    .multiply(new BigDecimal("5"), MC)
                    .divide(new BigDecimal("9"), MC);
            case KELVIN -> value.subtract(new BigDecimal("273.15"), MC);
        };

        BigDecimal result = switch (to) {
            case CELSIUS -> celsius;
            case FAHRENHEIT -> celsius.multiply(new BigDecimal("9"), MC)
                    .divide(new BigDecimal("5"), MC)
                    .add(new BigDecimal("32"), MC);
            case KELVIN -> celsius.add(new BigDecimal("273.15"), MC);
        };
        return normalize(result);
    }

    public static BigDecimal convertVolume(BigDecimal value, VolumeUnit from, VolumeUnit to) {
        validateNotNull(value, from, to);
        BigDecimal inLiters = value.multiply(from.getToLitersFactor(), MC);
        return normalize(inLiters.divide(to.getToLitersFactor(), MC));
    }

    public static BigDecimal convertSpeed(BigDecimal value, SpeedUnit from, SpeedUnit to) {
        validateNotNull(value, from, to);
        BigDecimal inMs = value.multiply(from.getToMetersPerSecondFactor(), MC);
        return normalize(inMs.divide(to.getToMetersPerSecondFactor(), MC));
    }

    public static BigDecimal convertDataStorage(BigDecimal value, DataStorageUnit from, DataStorageUnit to) {
        validateNotNull(value, from, to);
        BigDecimal inBytes = value.multiply(from.getToBytesFactor(), MC);
        return normalize(inBytes.divide(to.getToBytesFactor(), MC));
    }

    public static BigDecimal convert(String categoryStr, String fromUnit, String toUnit, BigDecimal value) {
        if (categoryStr == null || categoryStr.isBlank()) {
            throw new UtilityException("A categoria de unidade é obrigatória.");
        }
        Category category;
        try {
            category = Category.valueOf(categoryStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UtilityException("Categoria de unidade inválida: " + categoryStr +
                    ". Valores aceitos: " + Arrays.toString(Category.values()));
        }

        return switch (category) {
            case LENGTH -> convertLength(value, LengthUnit.fromString(fromUnit), LengthUnit.fromString(toUnit));
            case MASS -> convertMass(value, MassUnit.fromString(fromUnit), MassUnit.fromString(toUnit));
            case TEMPERATURE -> convertTemperature(value, TemperatureUnit.fromString(fromUnit), TemperatureUnit.fromString(toUnit));
            case VOLUME -> convertVolume(value, VolumeUnit.fromString(fromUnit), VolumeUnit.fromString(toUnit));
            case SPEED -> convertSpeed(value, SpeedUnit.fromString(fromUnit), SpeedUnit.fromString(toUnit));
            case DATA_STORAGE -> convertDataStorage(value, DataStorageUnit.fromString(fromUnit), DataStorageUnit.fromString(toUnit));
        };
    }

    public static Map<String, List<String>> getAvailableUnitsByCategory() {
        Map<String, List<String>> map = new LinkedHashMap<>();
        map.put(Category.LENGTH.name(), Arrays.stream(LengthUnit.values())
                .map(u -> u.name() + " (" + u.getSymbol() + ")").toList());
        map.put(Category.MASS.name(), Arrays.stream(MassUnit.values())
                .map(u -> u.name() + " (" + u.getSymbol() + ")").toList());
        map.put(Category.TEMPERATURE.name(), Arrays.stream(TemperatureUnit.values())
                .map(u -> u.name() + " (" + u.getSymbol() + ")").toList());
        map.put(Category.VOLUME.name(), Arrays.stream(VolumeUnit.values())
                .map(u -> u.name() + " (" + u.getSymbol() + ")").toList());
        map.put(Category.SPEED.name(), Arrays.stream(SpeedUnit.values())
                .map(u -> u.name() + " (" + u.getSymbol() + ")").toList());
        map.put(Category.DATA_STORAGE.name(), Arrays.stream(DataStorageUnit.values())
                .map(u -> u.name() + " (" + u.getSymbol() + ")").toList());
        return map;
    }

    private static void validateNotNull(BigDecimal value, Object from, Object to) {
        if (value == null || from == null || to == null) {
            throw new UtilityException("O valor e as unidades de origem/destino não podem ser nulos.");
        }
    }

    private static BigDecimal normalize(BigDecimal value) {
        return value.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
    }
}
