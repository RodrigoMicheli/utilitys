package com.utilitys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SenhaVerificacaoRequest(
        @Schema(description = "Senha em texto puro para conferência", example = "MinhaSenha@123")
        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @Schema(description = "Hash BCrypt previamente gerado", example = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy")
        @NotBlank(message = "O hash é obrigatório")
        String hash
) {}
