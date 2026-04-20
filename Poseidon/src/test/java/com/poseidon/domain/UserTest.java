package com.poseidon.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Validator validator;
    private User user;

    @BeforeEach
    void setUp() {

//        on teste les validations @NotBlank :
//        1. Vérifier que les annotations fonctionnent réellement
//        Écrire @NotBlank ne garantit pas qu'elle est bien prise en compte. Un oubli de configuration, une mauvaise dépendance, ou un champ mal annoté peut la rendre silencieuse.
//        2. Protéger contre les régressions
//        Si quelqu'un supprime accidentellement une annotation @NotBlank lors d'un refactoring, le test échoue immédiatement et alerte l'équipe.
//        3. Vérifier les messages d'erreur
//        On teste aussi que le message est exactement "Username is mandatory" et pas une autre chaîne. C'est important si ces messages sont affichés à l'utilisateur ou utilisés côté front.
//        4. Couvrir les cas limites de @NotBlank
//        @NotBlank rejette null, "", mais aussi "   " (espaces). Ce comportement mérite d'être confirmé, car @NotNull ou @NotEmpty se comportent différemment.

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Création d'un User valide de base
        user = new User();
        user.setId(1);
        user.setUsername("awatef");
        user.setPassword("123456");
        user.setFullname("DRIDI");
        user.setRole("USER");
    }

    // ===== Tests des getters/setters =====

    @Test
    void testGetSetId() {
        user.setId(42);
        assertEquals(42, user.getId());
    }

    @Test
    void testGetSetUsername() {
        user.setUsername("awatef");
        assertEquals("awatef", user.getUsername());
    }

    @Test
    void testGetSetPassword() {
        user.setPassword("newpwd");
        assertEquals("newpwd", user.getPassword());
    }

    @Test
    void testGetSetFullname() {
        user.setFullname("DRIDI");
        assertEquals("DRIDI", user.getFullname());
    }

    @Test
    void testGetSetRole() {
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    // ===== Tests de validation =====

    @Test
    void testValidUser_shouldHaveNoViolations() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsername_blank_shouldFail() {
        user.setUsername("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Username is mandatory")));
    }

    @Test
    void testUsername_null_shouldFail() {
        user.setUsername(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void testPassword_blank_shouldFail() {
        user.setPassword("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Password is mandatory")));
    }

    @Test
    void testFullname_blank_shouldFail() {
        user.setFullname("   "); // espaces seulement = blank
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("FullName is mandatory")));
    }

    @Test
    void testRole_blank_shouldFail() {
        user.setRole("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Role is mandatory")));
    }

    @Test
    void testMultipleFieldsBlank_shouldHaveMultipleViolations() {
        user.setUsername("");
        user.setPassword("");
        user.setFullname("");
        user.setRole("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(4, violations.size());
    }
}