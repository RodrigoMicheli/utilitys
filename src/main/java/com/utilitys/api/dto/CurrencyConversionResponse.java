package com.utilitys.api.dto;

import java.math.BigDecimal;

public record CurrencyConversionResponse(
        String fromCurrency,
        String toCurrency,
        BigDecimal originalAmount,
        String formattedOriginal,
        BigDecimal convertedAmount,
        String formattedConverted,
        BigDecimal appliedExchangeRate,
        String convertedAmountInWordsPtBr
) {}
