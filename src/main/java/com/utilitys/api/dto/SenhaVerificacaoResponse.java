package com.utilitys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SenhaVerificacaoResponse(
        @Schema(description = "Indica se a senha corresponde ao hash BCrypt informado", example = "true")
        boolean valida,

        @Schema(description = "Mensagem descritiva do resultado", example = "A senha corresponde ao hash informado.")
        String mensagem
) {}
