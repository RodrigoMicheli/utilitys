package com.utilitys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record NumeroExtensoRequest(
        @Schema(description = "Valor numérico a ser escrito por extenso", example = "1250.75")
        @NotNull(message = "O número é obrigatório")
        BigDecimal valor,

        @Schema(description = "Se verdadeiro, formata como valor monetário (Reais e Centavos); se falso, como número cardinal", example = "true")
        boolean monetario,

        @Schema(description = "Nome da moeda no singular (opcional, padrão: real)", example = "real", nullable = true)
        String moedaSingular,

        @Schema(description = "Nome da moeda no plural (opcional, padrão: reais)", example = "reais", nullable = true)
        String moedaPlural
) {}
