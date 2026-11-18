package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumeroPorExtensoUtilTest {

    @Test
    void shouldConvertCardinalIntegers() {
        assertEquals("zero", NumeroPorExtensoUtil.converterInteiro(0));
        assertEquals("cem", NumeroPorExtensoUtil.converterInteiro(100));
        assertEquals("cento e um", NumeroPorExtensoUtil.converterInteiro(101));
        assertEquals("mil", NumeroPorExtensoUtil.converterInteiro(1000));
        assertEquals("mil duzentos e cinquenta", NumeroPorExtensoUtil.converterInteiro(1250));
        assertEquals("mil e um", NumeroPorExtensoUtil.converterInteiro(1001));
        assertEquals("um milhão", NumeroPorExtensoUtil.converterInteiro(1_000_000));
        assertEquals("menos quarenta e dois", NumeroPorExtensoUtil.converterInteiro(-42));
    }

    @Test
    void shouldConvertCardinalDecimals() {
        assertEquals("um vírgula zero cinco", NumeroPorExtensoUtil.converterCardinal(new BigDecimal("1.05")));
        assertEquals(" doze vírgula cinco".trim(), NumeroPorExtensoUtil.converterCardinal(new BigDecimal("12.50")));
        assertThrows(UtilityException.class, () -> NumeroPorExtensoUtil.converterCardinal(null));
    }

    @Test
    void shouldConvertCurrencyValuesToWords() {
        assertEquals("zero reais", NumeroPorExtensoUtil.converterMoeda(BigDecimal.ZERO));
        assertEquals("um real", NumeroPorExtensoUtil.converterMoeda(new BigDecimal("1.00")));
        assertEquals("mil duzentos e cinquenta reais e setenta e cinco centavos",
                NumeroPorExtensoUtil.converterMoeda(new BigDecimal("1250.75")));
        assertEquals("um milhão de reais",
                NumeroPorExtensoUtil.converterMoeda(new BigDecimal("1000000.00")));
        assertEquals("cinquenta centavos",
                NumeroPorExtensoUtil.converterMoeda(new BigDecimal("0.50")));
        assertEquals("um centavo",
                NumeroPorExtensoUtil.converterMoeda(new BigDecimal("0.01")));
    }
}
