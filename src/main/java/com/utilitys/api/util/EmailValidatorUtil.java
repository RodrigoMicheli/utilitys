package com.utilitys.api.util;
import java.util.regex.Pattern;
/**
 * Classe utilitária estática para validação de formato de e-mail.
 *
 * Valida um subconjunto prático da RFC 5322 / RFC 5321:
 * - parte local com caracteres atômicos permitidos, sem ponto inicial/final e sem pontos consecutivos;
 * - domínio com rótulos alfanuméricos (hífen permitido no meio), ao menos um ponto e TLD só com letras;
 * - limites de tamanho: parte local até 64 caracteres, endereço completo até 254.
 *
 * Não verifica se o domínio existe nem se a caixa de entrada está ativa.
 */
public final class EmailValidatorUtil {
    private static final int MAX_LOCAL_LENGTH = 64;
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final String LOCAL_ATOM = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+";
    private static final String LABEL = "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^" + LOCAL_ATOM + "(?:\\." + LOCAL_ATOM + ")*"   // parte local
            + "@"
            + "(?:" + LABEL + "\\.)+"                          // rótulos do domínio
            + "[A-Za-z]{2,63}"                                 // TLD
            + "$"
    );
    private EmailValidatorUtil() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }
    /**
     * Indica se o texto informado tem formato válido de e-mail.
     *
     * @param email texto a validar; pode ser {@code null}
     * @return {@code true} se o formato for válido, {@code false} caso contrário
     */
    public static boolean isValid(String email) {
        if (email == null || email.length() > MAX_EMAIL_LENGTH) {
            return false;
        }
        int at = email.lastIndexOf('@');
        if (at <= 0 || at > MAX_LOCAL_LENGTH) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}