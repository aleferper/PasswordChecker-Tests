import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de caja negra de la configuración de {@link PasswordChecker}:
 * longitudes mínima y máxima y flags de comprobación.
 */
@DisplayName("PasswordChecker · configuración")
public class PasswordCheckerConfigTest {

    private PasswordChecker checker;

    @BeforeEach
    void setUp() {
        checker = new PasswordChecker();
    }

    // ---------- Valores por defecto documentados ----------

    @Test
    @DisplayName("Las comprobaciones están desactivadas por defecto")
    void comprobacionesDesactivadasPorDefecto() {
        assertFalse(checker.getCheckDigits());
        assertFalse(checker.getCheckLowers());
        assertFalse(checker.getCheckUppers());
    }

    // ---------- setMinLength ----------

    @ParameterizedTest(name = "CV7 · setMinLength({0}) es aceptado")
    @ValueSource(ints = {1, 8, 100})
    void minLengthValida(int min) {
        checker.setMaxLength(Integer.MAX_VALUE);
        checker.setMinLength(min);
        assertEquals(min, checker.getMinLength());
    }

    @ParameterizedTest(name = "CNV8 · setMinLength({0}) lanza IllegalArgumentException")
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void minLengthNoValida(int min) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> checker.setMinLength(min));
        assertEquals(PasswordChecker.ERR_INVALID_MIN_LENGTH, ex.getMessage());
    }

    // ---------- setMaxLength ----------

    @ParameterizedTest(name = "CV8 · setMaxLength({0}) con mínima 8 es aceptado")
    @ValueSource(ints = {8, 9, 16, Integer.MAX_VALUE})
    void maxLengthValida(int max) {
        checker.setMinLength(8);
        checker.setMaxLength(max);
        assertEquals(max, checker.getMaxLength());
    }

    @ParameterizedTest(name = "CNV9 · setMaxLength({0}) con mínima 8 lanza IllegalArgumentException")
    @ValueSource(ints = {7, 1, 0, -5})
    void maxLengthNoValida(int max) {
        checker.setMinLength(8);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> checker.setMaxLength(max));
        assertEquals(PasswordChecker.ERR_INVALID_MAX_LENGTH, ex.getMessage());
    }

    // ---------- Flags de comprobación ----------

    @Test
    @DisplayName("setCheckDigits / getCheckDigits")
    void flagDigitos() {
        checker.setCheckDigits(true);
        assertTrue(checker.getCheckDigits());
        checker.setCheckDigits(false);
        assertFalse(checker.getCheckDigits());
    }

    @Test
    @DisplayName("setCheckLowers / getCheckLowers")
    void flagMinusculas() {
        checker.setCheckLowers(true);
        assertTrue(checker.getCheckLowers());
        checker.setCheckLowers(false);
        assertFalse(checker.getCheckLowers());
    }

    @Test
    @DisplayName("setCheckUppers / getCheckUppers")
    void flagMayusculas() {
        checker.setCheckUppers(true);
        assertTrue(checker.getCheckUppers());
        checker.setCheckUppers(false);
        assertFalse(checker.getCheckUppers());
    }
}
