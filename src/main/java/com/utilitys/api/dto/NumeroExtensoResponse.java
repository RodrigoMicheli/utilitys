package com.utilitys.api.dto;

import java.math.BigDecimal;

public record NumeroExtensoResponse(
        BigDecimal valorOriginal,
        String tipo,
        String extenso,
        String extensoMaiusculo,
        String extensoCapitalizado
) {}
