package com.utilitys.api.dto;

import java.math.BigDecimal;

public record UnitConversionResponse(
        String category,
        String fromUnit,
        String toUnit,
        BigDecimal originalValue,
        BigDecimal convertedValue,
        String formattedSummary
) {}
