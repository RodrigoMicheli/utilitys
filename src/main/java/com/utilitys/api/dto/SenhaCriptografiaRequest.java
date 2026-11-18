package com.utilitys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SenhaCriptografiaRequest(
        @Schema(description = "Senha de acesso a ser criptografada", example = "MinhaSenha@123")
        @NotBlank(message = "A senha é obrigatória")
        String senha
) {}
