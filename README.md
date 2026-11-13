# Utilitys API - Central de Utilitários (Java 21 + Spring Boot 3)

API REST desenvolvida do zero em **Java 21** com **Spring Boot 3.4** que centraliza classes utilitárias para:
1. **Conversão de Unidades** (`UnitConverterUtil`) — Comprimento, Massa/Peso, Temperatura, Volume, Velocidade e Armazenamento de Dados.
2. **Moedas e Cálculos Financeiros** (`CurrencyUtil`) — Conversão de moedas (com cotação de referência ou taxa customizada), formatação monetária e cálculo de desconto, juros simples e juros compostos.
3. **Números e Valores Monetários por Extenso em PT-BR** (`NumeroPorExtensoUtil`) — Escrita por extenso de números cardinais (inteiros e decimais) e valores monetários até a casa dos trilhões.

---

## Pré-requisitos
- **Java 21** (JDK 21+)
- **Maven 3.9+**

## Como Executar

```bash
mvn spring-boot:run
```

Após iniciar, a aplicação ficará disponível em `http://localhost:8080`.

### Documentação Interativa (Swagger UI)
Acesse no navegador:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/api-docs`

---

## Principais Endpoints da API

### 1. Conversão de Unidades (`/api/v1/units`)
- **`GET /api/v1/units/categories`** — Lista todas as categorias e unidades disponíveis.
- **`GET /api/v1/units/convert?category=LENGTH&from=KILOMETER&to=METER&value=2.5`** — Converte `2.5 km` para `2500 m`.
- **`POST /api/v1/units/convert`** — Conversão via JSON:
  ```json
  {
    "category": "TEMPERATURE",
    "fromUnit": "CELSIUS",
    "toUnit": "FAHRENHEIT",
    "value": 100
  }
  ```

### 2. Conversão de Moedas e Cálculos Financeiros (`/api/v1/currencies`)
- **`GET /api/v1/currencies`** — Lista as moedas suportadas (`BRL`, `USD`, `EUR`, `GBP`, `JPY`, `ARS`, `CAD`, `CHF`).
- **`GET /api/v1/currencies/convert?from=USD&to=BRL&amount=100&customRate=5.80`** — Converte valores monetários e já retorna o resultado formatado e escrito por extenso.
- **`POST /api/v1/currencies/financial-calc`** — Calcula desconto, juros simples e juros compostos:
  ```json
  {
    "principal": 1000.00,
    "ratePercent": 2.5,
    "periods": 12
  }
  ```

### 3. Números por Extenso (`/api/v1/numbers`)
- **`GET /api/v1/numbers/extenso?valor=1250.75&monetario=true`**
  - Retorno: `"mil duzentos e cinquenta reais e setenta e cinco centavos"`
- **`POST /api/v1/numbers/extenso`**
  ```json
  {
    "valor": 1250.75,
    "monetario": true,
    "moedaSingular": "real",
    "moedaPlural": "reais"
  }
  ```

---

## Uso Direto das Classes Utilitárias no Código Java

Além dos endpoints REST, todas as regras estão encapsuladas em classes utilitárias estáticas (`final` com construtor privado) no pacote `com.utilitys.api.util`:

```java
BigDecimal milhas = UnitConverterUtil.convert("LENGTH", "km", "mi", new BigDecimal("10"));
BigDecimal valorConvertido = CurrencyUtil.convert(new BigDecimal("100"), SupportedCurrency.USD, SupportedCurrency.BRL);
String extenso = NumeroPorExtensoUtil.converterMoeda(new BigDecimal("1250.75"));
```
