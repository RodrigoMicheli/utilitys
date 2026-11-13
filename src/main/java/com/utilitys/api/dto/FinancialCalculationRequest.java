package com.utilitys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record FinancialCalculationRequest(
        @Schema(description = "Valor principal / inicial", example = "1000.00")
        @NotNull(message = "O valor principal é obrigatório")
        @PositiveOrZero(message = "O valor principal deve ser maior ou igual a zero")
        BigDecimal principal,

        @Schema(description = "Taxa percentual (desconto ou juros por período)", example = "2.5")
        @NotNull(message = "A taxa percentual é obrigatória")
        @PositiveOrZero(message = "A taxa percentual não pode ser negativa")
        BigDecimal ratePercent,

        @Schema(description = "Número de períodos (meses/anos) para cálculo de juros", example = "12")
        @Min(value = 0, message = "O número de períodos não pode ser negativo")
        @Max(value = 1200, message = "O número máximo de períodos é 1200")
        int periods
) {}
