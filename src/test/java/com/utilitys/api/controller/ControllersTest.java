package com.utilitys.api.controller;

import com.utilitys.api.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ControllersTest {

    @Test
    void shouldTestUnitConverterEndpoints() {
        UnitConverterController controller = new UnitConverterController();

        ResponseEntity<Map<String, List<String>>> categories = controller.listCategoriesAndUnits();
        assertEquals(HttpStatus.OK, categories.getStatusCode());
        assertNotNull(categories.getBody());
        assertEquals(6, categories.getBody().size());

        ResponseEntity<UnitConversionResponse> getResp = controller.convertGet("LENGTH", "km", "m", new BigDecimal("3"));
        assertEquals(HttpStatus.OK, getResp.getStatusCode());
        assertNotNull(getResp.getBody());
        assertEquals(0, new BigDecimal("3000").compareTo(getResp.getBody().convertedValue()));

        ResponseEntity<UnitConversionResponse> postResp = controller.convertPost(
                new UnitConversionRequest("TEMPERATURE", "C", "F", new BigDecimal("0"))
        );
        assertEquals(HttpStatus.OK, postResp.getStatusCode());
        assertNotNull(postResp.getBody());
        assertEquals(0, new BigDecimal("32").compareTo(postResp.getBody().convertedValue()));
    }

    @Test
    void shouldTestCurrencyEndpoints() {
        CurrencyController controller = new CurrencyController();

        ResponseEntity<Map<String, Map<String, Object>>> listResp = controller.listSupportedCurrencies();
        assertEquals(HttpStatus.OK, listResp.getStatusCode());
        assertNotNull(listResp.getBody());
        assertTrue(listResp.getBody().containsKey("BRL"));

        ResponseEntity<CurrencyConversionResponse> convResp = controller.convertGet(
                "USD", "BRL", new BigDecimal("100.00"), new BigDecimal("5.50")
        );
        assertEquals(HttpStatus.OK, convResp.getStatusCode());
        assertNotNull(convResp.getBody());
        assertEquals(new BigDecimal("550.00"), convResp.getBody().convertedAmount());
        assertEquals("quinhentos e cinquenta reais", convResp.getBody().convertedAmountInWordsPtBr());

        ResponseEntity<FinancialCalculationResponse> finResp = controller.calculateFinancials(
                new FinancialCalculationRequest(new BigDecimal("1000.00"), new BigDecimal("10"), 2)
        );
        assertEquals(HttpStatus.OK, finResp.getStatusCode());
        assertNotNull(finResp.getBody());
        assertEquals(new BigDecimal("900.00"), finResp.getBody().discountedValue());
        assertEquals(new BigDecimal("1200.00"), finResp.getBody().simpleInterestTotal());
        assertEquals(new BigDecimal("1210.00"), finResp.getBody().compoundInterestTotal());
    }

    @Test
    void shouldTestNumeroPorExtensoEndpoints() {
        NumeroPorExtensoController controller = new NumeroPorExtensoController();

        ResponseEntity<NumeroExtensoResponse> getResp = controller.converterGet(new BigDecimal("1500.50"), true);
        assertEquals(HttpStatus.OK, getResp.getStatusCode());
        assertNotNull(getResp.getBody());
        assertEquals("mil e quinhentos reais e cinquenta centavos", getResp.getBody().extenso());

        ResponseEntity<NumeroExtensoResponse> postResp = controller.converterPost(
                new NumeroExtensoRequest(new BigDecimal("42"), false, null, null)
        );
        assertEquals(HttpStatus.OK, postResp.getStatusCode());
        assertNotNull(postResp.getBody());
        assertEquals("quarenta e dois", postResp.getBody().extenso());
    }

    @Test
    void shouldTestPasswordEndpoints() {
        PasswordController controller = new PasswordController();

        ResponseEntity<String> generatedResp = controller.generate(16);
        assertEquals(HttpStatus.OK, generatedResp.getStatusCode());
        assertNotNull(generatedResp.getBody());
        assertEquals(16, generatedResp.getBody().length());

        String senha = generatedResp.getBody();
        ResponseEntity<String> encryptResp = controller.encrypt(new SenhaCriptografiaRequest(senha));
        assertEquals(HttpStatus.OK, encryptResp.getStatusCode());
        String hash = encryptResp.getBody();
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2"));

        ResponseEntity<SenhaVerificacaoResponse> verifyTrue = controller.verify(new SenhaVerificacaoRequest(senha, hash));
        assertEquals(HttpStatus.OK, verifyTrue.getStatusCode());
        assertNotNull(verifyTrue.getBody());
        assertTrue(verifyTrue.getBody().valida());

        ResponseEntity<SenhaVerificacaoResponse> verifyFalse = controller.verify(new SenhaVerificacaoRequest("OutraSenha123", hash));
        assertEquals(HttpStatus.OK, verifyFalse.getStatusCode());
        assertNotNull(verifyFalse.getBody());
        assertFalse(verifyFalse.getBody().valida());
    }
}
