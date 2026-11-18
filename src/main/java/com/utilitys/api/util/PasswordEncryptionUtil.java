package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilitário para proteção, verificação e geração de senhas de acesso.
 * Usa BCrypt (hash unidirecional com salt aleatório), que é o padrão recomendado para senhas:
 * o valor retornado não pode ser revertido para a senha original.
 */
public final class PasswordEncryptionUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final String UPPER = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijkmnopqrstuvwxyz";
    private static final String DIGITS = "23456789";
    private static final String SPECIAL = "@#$%&*!?+-=";
    private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SPECIAL;

    private PasswordEncryptionUtil() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }

    /**
     * Gera o valor criptografado (hash BCrypt) da senha informada.
     * Cada chamada gera um resultado diferente por causa do salt aleatório.
     *
     * @param senha senha em texto puro
     * @return hash BCrypt da senha
     * @throws UtilityException se a senha for nula ou vazia
     */
    public static String encrypt(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new UtilityException("A senha é obrigatória.");
        }
        return ENCODER.encode(senha);
    }

    /**
     * Verifica se uma senha em texto puro corresponde a um hash BCrypt previamente gerado.
     *
     * @param senha senha em texto puro
     * @param hash  hash BCrypt
     * @return true se a senha corresponder ao hash, false caso contrário
     * @throws UtilityException se a senha ou o hash forem nulos ou vazios
     */
    public static boolean matches(String senha, String hash) {
        if (senha == null || senha.isEmpty()) {
            throw new UtilityException("A senha é obrigatória.");
        }
        if (hash == null || hash.isBlank()) {
            throw new UtilityException("O hash BCrypt é obrigatório.");
        }
        return ENCODER.matches(senha, hash);
    }

    /**
     * Gera uma senha forte aleatória contendo letras maiúsculas, minúsculas, números e caracteres especiais.
     *
     * @param length tamanho desejado (entre 8 e 128 caracteres)
     * @return senha aleatória gerada com SecureRandom
     */
    public static String generateRandomPassword(int length) {
        if (length < 8 || length > 128) {
            throw new UtilityException("O tamanho da senha deve estar entre 8 e 128 caracteres.");
        }
        List<Character> chars = new ArrayList<>(length);
        chars.add(UPPER.charAt(SECURE_RANDOM.nextInt(UPPER.length())));
        chars.add(LOWER.charAt(SECURE_RANDOM.nextInt(LOWER.length())));
        chars.add(DIGITS.charAt(SECURE_RANDOM.nextInt(DIGITS.length())));
        chars.add(SPECIAL.charAt(SECURE_RANDOM.nextInt(SPECIAL.length())));

        for (int i = 4; i < length; i++) {
            chars.add(ALL_CHARS.charAt(SECURE_RANDOM.nextInt(ALL_CHARS.length())));
        }
        Collections.shuffle(chars, SECURE_RANDOM);

        StringBuilder sb = new StringBuilder(length);
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
