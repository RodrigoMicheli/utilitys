package com.utilitys.api.util;

import com.utilitys.api.util.CurrencyUtil.SupportedCurrency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyUtilTest {

    @Test
    void shouldConvertWithCustomRate() {
        BigDecimal converted = CurrencyUtil.convertWithCustomRate(new BigDecimal("100.00"), new BigDecimal("5.80"));
        assertEquals(new BigDecimal("580.00"), converted);
    }

    @Test
    void shouldConvertBetweenCurrenciesUsingReferenceRate() {
        BigDecimal converted = CurrencyUtil.convert(new BigDecimal("100.00"), SupportedCurrency.USD, SupportedCurrency.BRL);
        assertEquals(new BigDecimal("575.00"), converted);
    }

    @Test
    void shouldApplyDiscountAndCalculateInterest() {
        BigDecimal discounted = CurrencyUtil.applyDiscount(new BigDecimal("200.00"), new BigDecimal("15"));
        assertEquals(new BigDecimal("170.00"), discounted);

        BigDecimal simpleTotal = CurrencyUtil.calculateSimpleInterestTotal(new BigDecimal("1000.00"), new BigDecimal("2"), 10);
        assertEquals(new BigDecimal("1200.00"), simpleTotal);
    }
}
