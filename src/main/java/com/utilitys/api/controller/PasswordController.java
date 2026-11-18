package com.utilitys.api.controller;

import com.utilitys.api.dto.SenhaCriptografiaRequest;
import com.utilitys.api.dto.SenhaVerificacaoRequest;
import com.utilitys.api.dto.SenhaVerificacaoResponse;
import com.utilitys.api.util.PasswordEncryptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passwords")
@Tag(name = "4. Senhas", description = "Endpoints para criptografia, verificação e geração de senhas de acesso")
public class PasswordController {

    @PostMapping("/encrypt")
    @Operation(summary = "Criptografar senha de acesso",
            description = "Recebe uma senha no corpo da requisição (não na URL, para evitar exposição em logs) e retorna o valor criptografado (hash BCrypt) como String.")
    public ResponseEntity<String> encrypt(@Valid @RequestBody SenhaCriptografiaRequest request) {
        return ResponseEntity.ok(PasswordEncryptionUtil.encrypt(request.senha()));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verificar senha contra hash BCrypt",
            description = "Confere se uma senha em texto puro corresponde a um hash BCrypt gerado anteriormente.")
    public ResponseEntity<SenhaVerificacaoResponse> verify(@Valid @RequestBody SenhaVerificacaoRequest request) {
        boolean valida = PasswordEncryptionUtil.matches(request.senha(), request.hash());
        String mensagem = valida
                ? "A senha corresponde ao hash informado."
                : "A senha não corresponde ao hash informado.";
        return ResponseEntity.ok(new SenhaVerificacaoResponse(valida, mensagem));
    }

    @GetMapping("/generate")
    @Operation(summary = "Gerar senha aleatória segura",
            description = "Gera uma senha aleatória forte contendo letras maiúsculas, minúsculas, números e símbolos especiais.")
    public ResponseEntity<String> generate(
            @Parameter(description = "Tamanho da senha (entre 8 e 128 caracteres)", example = "16")
            @RequestParam(defaultValue = "16") int length
    ) {
        return ResponseEntity.ok(PasswordEncryptionUtil.generateRandomPassword(length));
    }
}
