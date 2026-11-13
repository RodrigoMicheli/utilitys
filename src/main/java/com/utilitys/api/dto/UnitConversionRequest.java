package com.utilitys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UnitConversionRequest(
        @Schema(description = "Categoria da grandeza", example = "LENGTH", allowableValues = {"LENGTH", "MASS", "TEMPERATURE", "VOLUME", "SPEED", "DATA_STORAGE"})
        @NotBlank(message = "A categoria é obrigatória")
        String category,

        @Schema(description = "Unidade de origem (nome ou símbolo)", example = "KILOMETER")
        @NotBlank(message = "A unidade de origem é obrigatória")
        String fromUnit,

        @Schema(description = "Unidade de destino (nome ou símbolo)", example = "MILE")
        @NotBlank(message = "A unidade de destino é obrigatória")
        String toUnit,

        @Schema(description = "Valor numérico a ser convertido", example = "10")
        @NotNull(message = "O valor é obrigatório")
        BigDecimal value
) {}
