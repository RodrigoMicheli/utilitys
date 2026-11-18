package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;
import com.utilitys.api.util.CurrencyUtil.SupportedCurrency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

        BigDecimal sameCurrency = CurrencyUtil.convert(new BigDecimal("50"), SupportedCurrency.EUR, SupportedCurrency.EUR);
        assertEquals(new BigDecimal("50.00"), sameCurrency);
    }

    @Test
    void shouldApplyDiscountAndCalculateInterest() {
        BigDecimal discounted = CurrencyUtil.applyDiscount(new BigDecimal("200.00"), new BigDecimal("15"));
        assertEquals(new BigDecimal("170.00"), discounted);

        BigDecimal simpleTotal = CurrencyUtil.calculateSimpleInterestTotal(new BigDecimal("1000.00"), new BigDecimal("2"), 10);
        assertEquals(new BigDecimal("1200.00"), simpleTotal);

        BigDecimal compoundTotal = CurrencyUtil.calculateCompoundInterestTotal(new BigDecimal("1000.00"), new BigDecimal("10"), 2);
        assertEquals(new BigDecimal("1210.00"), compoundTotal);
    }

    @Test
    void shouldFormatCurrencyAndListSupportedCurrencies() {
        String formatted = CurrencyUtil.formatCurrency(new BigDecimal("1234.56"), SupportedCurrency.USD);
        assertNotNull(formatted);
        assertTrue(formatted.contains("1,234.56"));

        Map<String, Map<String, Object>> info = CurrencyUtil.getSupportedCurrenciesInfo();
        assertTrue(info.containsKey("BRL"));
        assertTrue(info.containsKey("USD"));
        assertTrue(info.containsKey("EUR"));
    }

    @Test
    void shouldRejectInvalidCurrencyParameters() {
        assertThrows(UtilityException.class, () -> SupportedCurrency.fromCode("XYZ"));
        assertThrows(UtilityException.class, () -> CurrencyUtil.convert(new BigDecimal("-10"), SupportedCurrency.USD, SupportedCurrency.BRL));
        assertThrows(UtilityException.class, () -> CurrencyUtil.convertWithCustomRate(new BigDecimal("10"), BigDecimal.ZERO));
        assertThrows(UtilityException.class, () -> CurrencyUtil.applyDiscount(new BigDecimal("100"), new BigDecimal("150")));
    }
}
