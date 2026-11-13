package com.utilitys.api.controller;

import com.utilitys.api.dto.UnitConversionRequest;
import com.utilitys.api.dto.UnitConversionResponse;
import com.utilitys.api.util.UnitConverterUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/units")
@Tag(name = "1. Conversão de Unidades", description = "Endpoints para conversão de Comprimento, Massa, Temperatura, Volume, Velocidade e Armazenamento")
public class UnitConverterController {

    @GetMapping("/categories")
    @Operation(summary = "Listar categorias e unidades disponíveis",
            description = "Retorna todas as categorias de unidades suportadas e seus respectivos códigos/símbolos.")
    public ResponseEntity<Map<String, List<String>>> listCategoriesAndUnits() {
        return ResponseEntity.ok(UnitConverterUtil.getAvailableUnitsByCategory());
    }

    @GetMapping("/convert")
    @Operation(summary = "Converter unidade via parâmetros de consulta (GET)",
            description = "Converte um valor entre duas unidades da mesma categoria.")
    public ResponseEntity<UnitConversionResponse> convertGet(
            @Parameter(description = "Categoria (LENGTH, MASS, TEMPERATURE, VOLUME, SPEED, DATA_STORAGE)", example = "LENGTH")
            @RequestParam String category,
            @Parameter(description = "Unidade de origem (ex: KILOMETER, km, CELSIUS, C)", example = "KILOMETER")
            @RequestParam String from,
            @Parameter(description = "Unidade de destino (ex: METER, m, FAHRENHEIT, F)", example = "METER")
            @RequestParam String to,
            @Parameter(description = "Valor a converter", example = "2.5")
            @RequestParam BigDecimal value
    ) {
        return ResponseEntity.ok(buildConversionResponse(category, from, to, value));
    }

    @PostMapping("/convert")
    @Operation(summary = "Converter unidade via corpo JSON (POST)",
            description = "Converte um valor entre duas unidades da mesma categoria enviando um JSON.")
    public ResponseEntity<UnitConversionResponse> convertPost(@Valid @RequestBody UnitConversionRequest request) {
        return ResponseEntity.ok(buildConversionResponse(
                request.category(),
                request.fromUnit(),
                request.toUnit(),
                request.value()
        ));
    }

    private UnitConversionResponse buildConversionResponse(String category, String from, String to, BigDecimal value) {
        BigDecimal converted = UnitConverterUtil.convert(category, from, to, value);
        String summary = String.format("%s %s = %s %s",
                value.stripTrailingZeros().toPlainString(),
                from.toUpperCase(),
                converted.toPlainString(),
                to.toUpperCase());

        return new UnitConversionResponse(
                category.toUpperCase(),
                from.toUpperCase(),
                to.toUpperCase(),
                value,
                converted,
                summary
        );
    }
}
