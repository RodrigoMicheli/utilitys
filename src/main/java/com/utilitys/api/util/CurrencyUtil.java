package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Classe utilitária estática para conversão de moedas, formatação monetária
 * e cálculos financeiros (desconto, acréscimo, juros simples e juros compostos).
 */
public final class CurrencyUtil {

    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);

    private CurrencyUtil() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }

    /**
     * Moedas suportadas com taxas de referência padrão em relação ao Dólar Americano (USD = 1.00).
     * Também permite informar uma cotação customizada na chamada da API/método.
     */
    public enum SupportedCurrency {
        BRL("Real Brasileiro", "R$", Locale.of("pt", "BR"), new BigDecimal("5.75")),
        USD("Dólar Americano", "US$", Locale.US, BigDecimal.ONE),
        EUR("Euro", "€", Locale.GERMANY, new BigDecimal("0.93")),
        GBP("Libra Esterlina", "£", Locale.UK, new BigDecimal("0.79")),
        JPY("Iene Japonês", "¥", Locale.JAPAN, new BigDecimal("154.50")),
        ARS("Peso Argentino", "ARS$", Locale.of("es", "AR"), new BigDecimal("995.00")),
        CAD("Dólar Canadense", "CA$", Locale.CANADA, new BigDecimal("1.39")),
        CHF("Franco Suíço", "CHF", Locale.of("de", "CH"), new BigDecimal("0.88"));

        private final String description;
        private final String symbol;
        private final Locale locale;
        private final BigDecimal defaultRatePerUsd;

        SupportedCurrency(String description, String symbol, Locale locale, BigDecimal defaultRatePerUsd) {
            this.description = description;
            this.symbol = symbol;
            this.locale = locale;
            this.defaultRatePerUsd = defaultRatePerUsd;
        }

        public String getDescription() {
            return description;
        }

        public String getSymbol() {
            return symbol;
        }

        public Locale getLocale() {
            return locale;
        }

        public BigDecimal getDefaultRatePerUsd() {
            return defaultRatePerUsd;
        }

        public static SupportedCurrency fromCode(String code) {
            if (code == null || code.isBlank()) {
                throw new UtilityException("O código da moeda é obrigatório.");
            }
            for (SupportedCurrency currency : values()) {
                if (currency.name().equalsIgnoreCase(code.trim())) {
                    return currency;
                }
            }
            throw new UtilityException("Moeda não suportada: " + code +
                    ". Moedas disponíveis: " + Arrays.toString(values()));
        }
    }

    /**
     * Converte um valor entre duas moedas usando a taxa de referência padrão.
     */
    public static BigDecimal convert(BigDecimal amount, SupportedCurrency from, SupportedCurrency to) {
        validateAmount(amount);
        if (from == null || to == null) {
            throw new UtilityException("As moedas de origem e destino são obrigatórias.");
        }
        if (from == to) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal amountInUsd = amount.divide(from.getDefaultRatePerUsd(), MC);
        return amountInUsd.multiply(to.getDefaultRatePerUsd(), MC).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Converte um valor aplicando uma cotação/taxa de câmbio explícita (valor * exchangeRate).
     */
    public static BigDecimal convertWithCustomRate(BigDecimal amount, BigDecimal exchangeRate) {
        validateAmount(amount);
        if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new UtilityException("A taxa de câmbio deve ser maior que zero.");
        }
        return amount.multiply(exchangeRate, MC).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula a taxa de câmbio efetiva entre duas moedas suportadas.
     */
    public static BigDecimal getReferenceExchangeRate(SupportedCurrency from, SupportedCurrency to) {
        if (from == null || to == null) {
            throw new UtilityException("As moedas de origem e destino são obrigatórias.");
        }
        return to.getDefaultRatePerUsd()
                .divide(from.getDefaultRatePerUsd(), 6, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }

    /**
     * Formata um valor numérico no padrão monetário da moeda informada.
     */
    public static String formatCurrency(BigDecimal amount, SupportedCurrency currency) {
        if (amount == null || currency == null) {
            throw new UtilityException("Valor e moeda não podem ser nulos.");
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(currency.getLocale());
        return formatter.format(amount.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * Calcula o valor final após aplicar um percentual de desconto.
     */
    public static BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercent) {
        validateAmount(amount);
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(new BigDecimal("100")) > 0) {
            throw new UtilityException("O percentual de desconto deve estar entre 0 e 100.");
        }
        BigDecimal discountFactor = BigDecimal.ONE.subtract(discountPercent.divide(new BigDecimal("100"), MC));
        return amount.multiply(discountFactor, MC).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o montante final com juros simples: M = C * (1 + (i * t)).
     *
     * @param principal   Capital inicial
     * @param ratePercent Taxa de juros por período em percentual (ex: 2.5 para 2,5%)
     * @param periods     Número de períodos (meses/anos)
     */
    public static BigDecimal calculateSimpleInterestTotal(BigDecimal principal, BigDecimal ratePercent, int periods) {
        validateAmount(principal);
        validateInterestParams(ratePercent, periods);
        BigDecimal rateDecimal = ratePercent.divide(new BigDecimal("100"), MC);
        BigDecimal factor = BigDecimal.ONE.add(rateDecimal.multiply(BigDecimal.valueOf(periods), MC));
        return principal.multiply(factor, MC).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o montante final com juros compostos: M = C * (1 + i)^t.
     *
     * @param principal   Capital inicial
     * @param ratePercent Taxa de juros por período em percentual (ex: 1.5 para 1,5%)
     * @param periods     Número de períodos (meses/anos)
     */
    public static BigDecimal calculateCompoundInterestTotal(BigDecimal principal, BigDecimal ratePercent, int periods) {
        validateAmount(principal);
        validateInterestParams(ratePercent, periods);
        BigDecimal rateDecimal = ratePercent.divide(new BigDecimal("100"), MC);
        BigDecimal factor = BigDecimal.ONE.add(rateDecimal).pow(periods, MC);
        return principal.multiply(factor, MC).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Retorna o catálogo de moedas disponíveis e suas informações.
     */
    public static Map<String, Map<String, Object>> getSupportedCurrenciesInfo() {
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();
        for (SupportedCurrency c : SupportedCurrency.values()) {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("description", c.getDescription());
            info.put("symbol", c.getSymbol());
            info.put("referenceRatePerUsd", c.getDefaultRatePerUsd());
            result.put(c.name(), info);
        }
        return result;
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new UtilityException("O valor monetário não pode ser nulo ou negativo.");
        }
    }

    private static void validateInterestParams(BigDecimal ratePercent, int periods) {
        if (ratePercent == null || ratePercent.compareTo(BigDecimal.ZERO) < 0) {
            throw new UtilityException("A taxa de juros não pode ser nula ou negativa.");
        }
        if (periods < 0 || periods > 1200) {
            throw new UtilityException("O número de períodos deve estar entre 0 e 1200.");
        }
    }
}
