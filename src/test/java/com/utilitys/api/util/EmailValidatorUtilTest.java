package com.utilitys.api.util;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
class EmailValidatorUtilTest {
    @Test
    void shouldAcceptValidEmails() {
        assertTrue(EmailValidatorUtil.isValid("user@example.com"));
        assertTrue(EmailValidatorUtil.isValid("first.last+tag@sub.domain.co.uk"));
        assertTrue(EmailValidatorUtil.isValid("o'neil@my-domain.org"));
        assertTrue(EmailValidatorUtil.isValid("a@b.io"));
    }
    @Test
    void shouldRejectNullAndEmpty() {
        assertFalse(EmailValidatorUtil.isValid(null));
        assertFalse(EmailValidatorUtil.isValid(""));
        assertFalse(EmailValidatorUtil.isValid("   "));
    }
    @Test
    void shouldRejectMissingParts() {
        assertFalse(EmailValidatorUtil.isValid("user.example.com"));
        assertFalse(EmailValidatorUtil.isValid("@example.com"));
        assertFalse(EmailValidatorUtil.isValid("user@"));
        assertFalse(EmailValidatorUtil.isValid("user@example"));
    }
    @Test
    void shouldRejectInvalidDots() {
        assertFalse(EmailValidatorUtil.isValid(".user@example.com"));
        assertFalse(EmailValidatorUtil.isValid("user.@example.com"));
        assertFalse(EmailValidatorUtil.isValid("us..er@example.com"));
        assertFalse(EmailValidatorUtil.isValid("user@.example.com"));
        assertFalse(EmailValidatorUtil.isValid("user@example..com"));
        assertFalse(EmailValidatorUtil.isValid("user@example.com."));
    }
    @Test
    void shouldRejectInvalidDomainCharacters() {
        assertFalse(EmailValidatorUtil.isValid("user@-example.com"));
        assertFalse(EmailValidatorUtil.isValid("user@exam_ple.com"));
        assertFalse(EmailValidatorUtil.isValid("user@example.c0m"));
        assertFalse(EmailValidatorUtil.isValid("user@example.c"));
    }
    @Test
    void shouldRejectWhitespaceAndMultipleAts() {
        assertFalse(EmailValidatorUtil.isValid(" user@example.com"));
        assertFalse(EmailValidatorUtil.isValid("user@example.com "));
        assertFalse(EmailValidatorUtil.isValid("us er@example.com"));
        assertFalse(EmailValidatorUtil.isValid("user@@example.com"));
        assertFalse(EmailValidatorUtil.isValid("a@b@example.com"));
    }
    @Test
    void shouldEnforceLengthLimits() {
        String local64 = "a".repeat(64);
        String local65 = "a".repeat(65);
        assertTrue(EmailValidatorUtil.isValid(local64 + "@example.com"));
        assertFalse(EmailValidatorUtil.isValid(local65 + "@example.com"));
        String label63 = "a".repeat(63);
        String label64 = "a".repeat(64);
        assertTrue(EmailValidatorUtil.isValid("user@" + label63 + ".com"));
        assertFalse(EmailValidatorUtil.isValid("user@" + label64 + ".com"));

        // Limite total de 254 caracteres (64 local + 1 '@' + 189 domínio = 254)
        String domain189 = "a".repeat(61) + "." + "b".repeat(61) + "." + "c".repeat(61) + ".com";
        String email254 = local64 + "@" + domain189;
        assertTrue(EmailValidatorUtil.isValid(email254));
        assertFalse(EmailValidatorUtil.isValid(email254 + "a"));
    }

    @Test
    void shouldHandleAdditionalCasesAndSubdomains() {
        assertTrue(EmailValidatorUtil.isValid("USER.NAME@EMPRESA.COM.BR"));
        assertTrue(EmailValidatorUtil.isValid("contato_2026@sub1.sub2.dominio-teste.org"));
        assertFalse(EmailValidatorUtil.isValid("user@example-.com"));
        assertFalse(EmailValidatorUtil.isValid("user@-example-.com"));
    }
}