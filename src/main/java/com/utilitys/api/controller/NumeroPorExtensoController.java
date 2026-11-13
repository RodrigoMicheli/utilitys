package com.utilitys.api.controller;

import com.utilitys.api.dto.NumeroExtensoRequest;
import com.utilitys.api.dto.NumeroExtensoResponse;
import com.utilitys.api.util.NumeroPorExtensoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/numbers")
@Tag(name = "3. Números por Extenso", description = "Endpoints para converter números cardinais e valores monetários para texto por extenso em Português (PT-BR)")
public class NumeroPorExtensoController {

    @GetMapping("/extenso")
    @Operation(summary = "Converter número ou moeda para extenso (GET)",
            description = "Retorna a escrita por extenso em Português (PT-BR) de um número cardinal ou valor monetário em Reais.")
    public ResponseEntity<NumeroExtensoResponse> converterGet(
            @Parameter(description = "Valor numérico (ex: 1250.50)", example = "1250.50")
            @RequestParam BigDecimal valor,
            @Parameter(description = "Se true, converte como moeda (Reais e Centavos); se false, como número cardinal", example = "true")
            @RequestParam(defaultValue = "false") boolean monetario
    ) {
        return ResponseEntity.ok(buildResponse(valor, monetario, null, null));
    }

    @PostMapping("/extenso")
    @Operation(summary = "Converter número ou moeda customizada para extenso (POST)",
            description = "Permite converter número cardinal ou valor monetário com nomes de moeda customizados.")
    public ResponseEntity<NumeroExtensoResponse> converterPost(@Valid @RequestBody NumeroExtensoRequest request) {
        return ResponseEntity.ok(buildResponse(
                request.valor(),
                request.monetario(),
                request.moedaSingular(),
                request.moedaPlural()
        ));
    }

    private NumeroExtensoResponse buildResponse(BigDecimal valor, boolean monetario, String moedaSingular, String moedaPlural) {
        String extenso;
        if (monetario) {
            String singular = (moedaSingular != null && !moedaSingular.isBlank()) ? moedaSingular.trim() : "real";
            String plural = (moedaPlural != null && !moedaPlural.isBlank()) ? moedaPlural.trim() : "reais";
            extenso = NumeroPorExtensoUtil.converterMoeda(valor, singular, plural, "centavo", "centavos");
        } else {
            extenso = NumeroPorExtensoUtil.converterCardinal(valor);
        }

        String capitalizado = extenso.isEmpty()
                ? extenso
                : Character.toUpperCase(extenso.charAt(0)) + extenso.substring(1);

        return new NumeroExtensoResponse(
                valor,
                monetario ? "MONETARIO" : "CARDINAL",
                extenso,
                extenso.toUpperCase(),
                capitalizado
        );
    }
}
