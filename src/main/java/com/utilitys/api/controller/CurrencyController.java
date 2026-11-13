package com.utilitys.api.controller;

import com.utilitys.api.dto.CurrencyConversionRequest;
import com.utilitys.api.dto.CurrencyConversionResponse;
import com.utilitys.api.dto.FinancialCalculationRequest;
import com.utilitys.api.dto.FinancialCalculationResponse;
import com.utilitys.api.util.CurrencyUtil;
import com.utilitys.api.util.CurrencyUtil.SupportedCurrency;
import com.utilitys.api.util.NumeroPorExtensoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/currencies")
@Tag(name = "2. Moedas e Cálculos Financeiros", description = "Endpoints para conversão de moedas, formatação monetária e cálculos de desconto/juros")
public class CurrencyController {

    @GetMapping
    @Operation(summary = "Listar moedas suportadas",
            description = "Retorna a lista de moedas suportadas com símbolo, descrição e cotação de referência base (USD).")
    public ResponseEntity<Map<String, Map<String, Object>>> listSupportedCurrencies() {
        return ResponseEntity.ok(CurrencyUtil.getSupportedCurrenciesInfo());
    }

    @GetMapping("/convert")
    @Operation(summary = "Converter valor entre moedas (GET)",
            description = "Converte um valor monetário entre duas moedas usando a taxa de referência ou uma taxa customizada opcional.")
    public ResponseEntity<CurrencyConversionResponse> convertGet(
            @Parameter(description = "Moeda de origem (ex: USD, BRL, EUR)", example = "USD")
            @RequestParam String from,
            @Parameter(description = "Moeda de destino (ex: BRL, USD, EUR)", example = "BRL")
            @RequestParam String to,
            @Parameter(description = "Valor a ser convertido", example = "100.00")
            @RequestParam BigDecimal amount,
            @Parameter(description = "Cotação customizada opcional", example = "5.75")
            @RequestParam(required = false) BigDecimal customRate
    ) {
        return ResponseEntity.ok(executeConversion(from, to, amount, customRate));
    }

    @PostMapping("/convert")
    @Operation(summary = "Converter valor entre moedas (POST)",
            description = "Converte um valor monetário entre duas moedas enviando um corpo JSON.")
    public ResponseEntity<CurrencyConversionResponse> convertPost(@Valid @RequestBody CurrencyConversionRequest request) {
        return ResponseEntity.ok(executeConversion(
                request.fromCurrency(),
                request.toCurrency(),
                request.amount(),
                request.customExchangeRate()
        ));
    }

    @PostMapping("/financial-calc")
    @Operation(summary = "Calcular desconto, juros simples e juros compostos",
            description = "Calcula simultaneamente o valor com desconto, montante em juros simples e montante em juros compostos.")
    public ResponseEntity<FinancialCalculationResponse> calculateFinancials(@Valid @RequestBody FinancialCalculationRequest request) {
        BigDecimal principal = request.principal();
        BigDecimal rate = request.ratePercent();
        int periods = request.periods();

        BigDecimal discounted = rate.compareTo(new BigDecimal("100")) <= 0
                ? CurrencyUtil.applyDiscount(principal, rate)
                : BigDecimal.ZERO;

        BigDecimal simpleTotal = CurrencyUtil.calculateSimpleInterestTotal(principal, rate, periods);
        BigDecimal compoundTotal = CurrencyUtil.calculateCompoundInterestTotal(principal, rate, periods);

        return ResponseEntity.ok(new FinancialCalculationResponse(
                principal,
                rate,
                periods,
                discounted,
                simpleTotal,
                simpleTotal.subtract(principal),
                compoundTotal,
                compoundTotal.subtract(principal)
        ));
    }

    private CurrencyConversionResponse executeConversion(String fromCode, String toCode, BigDecimal amount, BigDecimal customRate) {
        SupportedCurrency from = SupportedCurrency.fromCode(fromCode);
        SupportedCurrency to = SupportedCurrency.fromCode(toCode);

        BigDecimal appliedRate;
        BigDecimal convertedAmount;

        if (customRate != null) {
            appliedRate = customRate;
            convertedAmount = CurrencyUtil.convertWithCustomRate(amount, customRate);
        } else {
            appliedRate = CurrencyUtil.getReferenceExchangeRate(from, to);
            convertedAmount = CurrencyUtil.convert(amount, from, to);
        }

        String formattedOriginal = CurrencyUtil.formatCurrency(amount, from);
        String formattedConverted = CurrencyUtil.formatCurrency(convertedAmount, to);

        String inWords = to == SupportedCurrency.BRL
                ? NumeroPorExtensoUtil.converterMoeda(convertedAmount)
                : NumeroPorExtensoUtil.converterMoeda(
                        convertedAmount,
                        to.getDescription().toLowerCase(),
                        to.getDescription().toLowerCase() + "s",
                        "centavo",
                        "centavos"
                );

        return new CurrencyConversionResponse(
                from.name(),
                to.name(),
                amount,
                formattedOriginal,
                convertedAmount,
                formattedConverted,
                appliedRate,
                inWords
        );
    }
}
