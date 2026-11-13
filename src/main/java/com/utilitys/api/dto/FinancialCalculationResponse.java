package com.utilitys.api.dto;

import java.math.BigDecimal;

public record FinancialCalculationResponse(
        BigDecimal principal,
        BigDecimal ratePercent,
        int periods,
        BigDecimal discountedValue,
        BigDecimal simpleInterestTotal,
        BigDecimal simpleInterestEarned,
        BigDecimal compoundInterestTotal,
        BigDecimal compoundInterestEarned
) {}
