package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária estática para escrita de números e valores monetários por extenso em Português (PT-BR).
 * Suporta valores inteiros, decimais e monetários (Real / Centavos) até a casa dos trilhões.
 */
public final class NumeroPorExtensoUtil {

    private static final long MAX_SUPPORTED = 999_999_999_999_999L;

    private static final String[] UNIDADES = {
            "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove",
            "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"
    };

    private static final String[] DEZENAS = {
            "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"
    };

    private static final String[] CENTENAS = {
            "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos",
            "seiscentos", "setecentos", "oitocentos", "novecentos"
    };

    private static final String[] ESCALAS_SINGULAR = {
            "", "mil", "milhão", "bilhão", "trilhão"
    };

    private static final String[] ESCALAS_PLURAL = {
            "", "mil", "milhões", "bilhões", "trilhões"
    };

    private NumeroPorExtensoUtil() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }

    /**
     * Converte um número inteiro (long) para sua representação cardinal por extenso.
     */
    public static String converterInteiro(long numero) {
        if (numero == 0) {
            return "zero";
        }
        boolean negativo = numero < 0;
        long valorAbsoluto = Math.abs(numero);

        if (valorAbsoluto > MAX_SUPPORTED) {
            throw new UtilityException("Número fora do limite suportado (máx: 999 trilhões).");
        }

        String extenso = converterInteiroPositivo(valorAbsoluto);
        return negativo ? "menos " + extenso : extenso;
    }

    /**
     * Converte um valor BigDecimal para número cardinal por extenso (incluindo casas decimais se houver).
     */
    public static String converterCardinal(BigDecimal valor) {
        if (valor == null) {
            throw new UtilityException("O valor não pode ser nulo.");
        }

        BigDecimal normalizado = valor.stripTrailingZeros();
        if (normalizado.scale() <= 0) {
            return converterInteiro(normalizado.longValueExact());
        }

        boolean negativo = normalizado.compareTo(BigDecimal.ZERO) < 0;
        BigDecimal abs = normalizado.abs();

        long parteInteira = abs.longValue();
        BigDecimal fracao = abs.subtract(BigDecimal.valueOf(parteInteira));
        String fracaoStr = fracao.toPlainString().substring(2); // remove "0."
        long valorFracao = Long.parseLong(fracaoStr);

        StringBuilder sb = new StringBuilder();
        if (negativo) {
            sb.append("menos ");
        }
        sb.append(converterInteiroPositivo(parteInteira));
        sb.append(" vírgula ");

        // Preserva zeros à esquerda após a vírgula (ex: 1,05 -> um vírgula zero cinco)
        int zerosEsquerda = 0;
        while (zerosEsquerda < fracaoStr.length() && fracaoStr.charAt(zerosEsquerda) == '0') {
            sb.append("zero ");
            zerosEsquerda++;
        }
        if (valorFracao > 0) {
            sb.append(converterInteiroPositivo(valorFracao));
        }

        return sb.toString().trim();
    }

    /**
     * Converte um valor monetário (BigDecimal) para extenso em Reais (BRL).
     */
    public static String converterMoeda(BigDecimal valor) {
        return converterMoeda(valor, "real", "reais", "centavo", "centavos");
    }

    /**
     * Converte um valor monetário para extenso com nomes de moeda customizados.
     */
    public static String converterMoeda(
            BigDecimal valor,
            String moedaSingular,
            String moedaPlural,
            String centavoSingular,
            String centavoPlural
    ) {
        if (valor == null) {
            throw new UtilityException("O valor monetário não pode ser nulo.");
        }

        BigDecimal arredondado = valor.setScale(2, RoundingMode.HALF_UP);
        boolean negativo = arredondado.compareTo(BigDecimal.ZERO) < 0;
        BigDecimal abs = arredondado.abs();

        long inteiros = abs.longValue();
        int centavos = abs.subtract(BigDecimal.valueOf(inteiros))
                .multiply(BigDecimal.valueOf(100))
                .intValue();

        if (inteiros > MAX_SUPPORTED) {
            throw new UtilityException("Valor fora do limite suportado (máx: 999 trilhões).");
        }

        if (inteiros == 0 && centavos == 0) {
            return "zero " + moedaPlural;
        }

        StringBuilder resultado = new StringBuilder();
        if (negativo) {
            resultado.append("menos ");
        }

        if (inteiros > 0) {
            resultado.append(converterInteiroPositivo(inteiros));
            if (precisaPreposicaoDe(inteiros)) {
                resultado.append(" de");
            }
            resultado.append(" ").append(inteiros == 1 ? moedaSingular : moedaPlural);
        }

        if (centavos > 0) {
            if (inteiros > 0) {
                resultado.append(" e ");
            }
            resultado.append(converterInteiroPositivo(centavos));
            resultado.append(" ").append(centavos == 1 ? centavoSingular : centavoPlural);
        }

        return resultado.toString();
    }

    private static boolean precisaPreposicaoDe(long numero) {
        // Ex: 1.000.000 -> "um milhão de reais", 2.000.000.000 -> "dois bilhões de reais"
        return numero >= 1_000_000 && (numero % 1_000_000 == 0);
    }

    private static String converterInteiroPositivo(long numero) {
        if (numero == 0) {
            return "zero";
        }

        List<Integer> grupos = new ArrayList<>();
        long temp = numero;
        while (temp > 0) {
            grupos.add((int) (temp % 1000));
            temp /= 1000;
        }

        List<String> partes = new ArrayList<>();
        List<Integer> valoresGrupo = new ArrayList<>();

        for (int i = grupos.size() - 1; i >= 0; i--) {
            int valorGrupo = grupos.get(i);
            if (valorGrupo == 0) {
                continue;
            }

            String textoGrupo;
            if (i == 1 && valorGrupo == 1) {
                // Em português dizemos "mil" e não "um mil"
                textoGrupo = "mil";
            } else {
                textoGrupo = converterGrupoDeTresDigitos(valorGrupo);
                if (i > 0) {
                    String escala = valorGrupo == 1 ? ESCALAS_SINGULAR[i] : ESCALAS_PLURAL[i];
                    textoGrupo += " " + escala;
                }
            }
            partes.add(textoGrupo);
            valoresGrupo.add(valorGrupo);
        }

        if (partes.size() == 1) {
            return partes.get(0);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partes.size(); i++) {
            if (i > 0) {
                boolean ultimoGrupo = (i == partes.size() - 1);
                int valorUltimo = valoresGrupo.get(i);
                // Usa " e " antes do último grupo se ele for menor que 100 ou múltiplo exato de 100
                if (ultimoGrupo && (valorUltimo < 100 || valorUltimo % 100 == 0)) {
                    sb.append(" e ");
                } else {
                    sb.append(" ");
                }
            }
            sb.append(partes.get(i));
        }

        return sb.toString();
    }

    private static String converterGrupoDeTresDigitos(int valor) {
        if (valor == 100) {
            return "cem";
        }

        int centena = valor / 100;
        int resto = valor % 100;
        int dezena = resto / 10;
        int unidade = resto % 10;

        StringBuilder sb = new StringBuilder();
        if (centena > 0) {
            sb.append(CENTENAS[centena]);
        }

        if (resto > 0) {
            if (centena > 0) {
                sb.append(" e ");
            }
            if (resto < 20) {
                sb.append(UNIDADES[resto]);
            } else {
                sb.append(DEZENAS[dezena]);
                if (unidade > 0) {
                    sb.append(" e ").append(UNIDADES[unidade]);
                }
            }
        }

        return sb.toString();
    }
}
