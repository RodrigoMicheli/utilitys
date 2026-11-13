package com.utilitys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CurrencyConversionRequest(
        @Schema(description = "Código da moeda de origem", example = "USD")
        @NotBlank(message = "A moeda de origem é obrigatória")
        String fromCurrency,

        @Schema(description = "Código da moeda de destino", example = "BRL")
        @NotBlank(message = "A moeda de destino é obrigatória")
        String toCurrency,

        @Schema(description = "Valor monetário a ser convertido", example = "100.00")
        @NotNull(message = "O valor é obrigatório")
        @PositiveOrZero(message = "O valor não pode ser negativo")
        BigDecimal amount,

        @Schema(description = "Taxa de câmbio customizada opcional (se omitida, usa a cotação de referência)", example = "5.80", nullable = true)
        BigDecimal customExchangeRate
) {}
