package com.utilitys.api.util;

import com.utilitys.api.exception.UtilityException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptionUtilTest {

    @Test
    void shouldReturnBcryptHashThatMatchesOriginalPassword() {
        String hash = PasswordEncryptionUtil.encrypt("MinhaSenha@123");
        assertNotEquals("MinhaSenha@123", hash);
        assertTrue(hash.startsWith("$2"));
        assertTrue(new BCryptPasswordEncoder().matches("MinhaSenha@123", hash));
    }

    @Test
    void shouldGenerateDifferentHashesForSamePassword() {
        assertNotEquals(
                PasswordEncryptionUtil.encrypt("abc12345"),
                PasswordEncryptionUtil.encrypt("abc12345"));
    }

    @Test
    void shouldRejectNullOrEmptyPassword() {
        assertThrows(UtilityException.class, () -> PasswordEncryptionUtil.encrypt(null));
        assertThrows(UtilityException.class, () -> PasswordEncryptionUtil.encrypt(""));
    }

    @Test
    void shouldVerifyPasswordAgainstBcryptHash() {
        String hash = PasswordEncryptionUtil.encrypt("Segredo#2026");
        assertTrue(PasswordEncryptionUtil.matches("Segredo#2026", hash));
        assertFalse(PasswordEncryptionUtil.matches("SenhaErrada", hash));
        assertThrows(UtilityException.class, () -> PasswordEncryptionUtil.matches(null, hash));
        assertThrows(UtilityException.class, () -> PasswordEncryptionUtil.matches("Segredo#2026", ""));
    }

    @Test
    void shouldGenerateStrongRandomPassword() {
        String generated = PasswordEncryptionUtil.generateRandomPassword(16);
        assertEquals(16, generated.length());
        assertTrue(generated.chars().anyMatch(Character::isUpperCase));
        assertTrue(generated.chars().anyMatch(Character::isLowerCase));
        assertTrue(generated.chars().anyMatch(Character::isDigit));
        assertThrows(UtilityException.class, () -> PasswordEncryptionUtil.generateRandomPassword(5));
        assertThrows(UtilityException.class, () -> PasswordEncryptionUtil.generateRandomPassword(200));
    }
}
