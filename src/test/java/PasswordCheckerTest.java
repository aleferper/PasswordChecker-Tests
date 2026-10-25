import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas de caja negra del método {@link PasswordChecker#check(String, String)}
 * diseñadas a partir de las clases de equivalencia y valores límite
 * descritas en el dossier.
 *
 * Configuración común (salvo que el test indique otra cosa):
 * longitud mínima 8, longitud máxima 16 y comprobación de dígitos,
 * minúsculas y mayúsculas activada.
 */
@DisplayName("PasswordChecker.check()")
public class PasswordCheckerTest {

    private static final int MIN = 8;
    private static final int MAX = 16;

    private PasswordChecker checker;

    @BeforeEach
    void setUp() {
        checker = new PasswordChecker();
        checker.setMinLength(MIN);
        checker.setMaxLength(MAX);
        checker.setCheckDigits(true);
        checker.setCheckLowers(true);
        checker.setCheckUppers(true);
    }

    /** Devuelve el resultado de check() repitiendo la misma contraseña. */
    private int check(String password) {
        return checker.check(password, password);
    }

    // ---------- Clases válidas ----------

    @Test
    @DisplayName("CV1 · Contraseña correcta -> PASSWORD_OK")
    void passwordCorrecto() {
        assertEquals(PasswordChecker.PASSWORD_OK, check("Abcdef12"));
    }

    @Test
    @DisplayName("CV2 · Sin comprobaciones activas, solo minúsculas -> PASSWORD_OK")
    void passwordSinComprobacionesActivas() {
        checker.setCheckDigits(false);
        checker.setCheckLowers(false);
        checker.setCheckUppers(false);
        assertEquals(PasswordChecker.PASSWORD_OK, check("abcdefgh"));
    }

    // ---------- Contraseña vacía o en blanco ----------

    @ParameterizedTest(name = "CNV1 · [{0}] -> ERR_BLANK")
    @ValueSource(strings = {"", " ", "        ", "\t\n"})
    void passwordEnBlanco(String password) {
        assertEquals(PasswordChecker.ERR_BLANK, check(password));
    }

    // ---------- Longitud ----------

    @ParameterizedTest(name = "CV3 · longitud límite válida [{0}] -> PASSWORD_OK")
    @ValueSource(strings = {
            "Abcdef12",          // longitud = MIN (8)
            "Abcdefgh12345678"   // longitud = MAX (16)
    })
    void passwordLongitudEnLimitesValidos(String password) {
        assertEquals(PasswordChecker.PASSWORD_OK, check(password));
    }

    @ParameterizedTest(name = "CNV2 · longitud menor que la mínima [{0}] -> ERR_LENGTH")
    @ValueSource(strings = {
            "A1b",               // muy corta (3)
            "Abcde12"            // longitud = MIN - 1 (7)
    })
    void passwordDemasiadoCorta(String password) {
        assertEquals(PasswordChecker.ERR_LENGTH, check(password));
    }

    @ParameterizedTest(name = "CNV3 · longitud mayor que la máxima [{0}] -> ERR_LENGTH")
    @ValueSource(strings = {
            "Abcdefgh123456789",                  // longitud = MAX + 1 (17)
            "Abcdefgh123456789Abcdefgh123456789"  // muy larga (34)
    })
    void passwordDemasiadoLarga(String password) {
        assertEquals(PasswordChecker.ERR_LENGTH, check(password));
    }

    // ---------- Dígitos ----------

    @Test
    @DisplayName("CNV4 · Sin dígitos con comprobación activa -> ERR_DIGITS")
    void passwordSinNumeros() {
        assertEquals(PasswordChecker.ERR_DIGITS, check("Abcdefgh"));
    }

    @Test
    @DisplayName("CV4 · Sin dígitos con comprobación desactivada -> PASSWORD_OK")
    void passwordSinNumerosSinComprobacion() {
        checker.setCheckDigits(false);
        assertEquals(PasswordChecker.PASSWORD_OK, check("Abcdefgh"));
    }

    // ---------- Minúsculas ----------

    @Test
    @DisplayName("CNV5 · Sin minúsculas con comprobación activa -> ERR_LOWERS")
    void passwordSinMinusculas() {
        assertEquals(PasswordChecker.ERR_LOWERS, check("ABCDEF12"));
    }

    @Test
    @DisplayName("CV5 · Sin minúsculas con comprobación desactivada -> PASSWORD_OK")
    void passwordSinMinusculasSinComprobacion() {
        checker.setCheckLowers(false);
        assertEquals(PasswordChecker.PASSWORD_OK, check("ABCDEF12"));
    }

    // ---------- Mayúsculas ----------

    @Test
    @DisplayName("CNV6 · Sin mayúsculas con comprobación activa -> ERR_UPPERS")
    void passwordSinMayusculas() {
        assertEquals(PasswordChecker.ERR_UPPERS, check("abcdef12"));
    }

    @Test
    @DisplayName("CV6 · Sin mayúsculas con comprobación desactivada -> PASSWORD_OK")
    void passwordSinMayusculasSinComprobacion() {
        checker.setCheckUppers(false);
        assertEquals(PasswordChecker.PASSWORD_OK, check("abcdef12"));
    }

    // ---------- Coincidencia con la repetición ----------

    @Test
    @DisplayName("CNV7 · Repetición distinta -> ERR_NO_MATCH")
    void passwordNoCoincide() {
        assertEquals(PasswordChecker.ERR_NO_MATCH, checker.check("Abcdef12", "Abcdef13"));
    }

    @Test
    @DisplayName("CNV7 · Repetición que solo difiere en mayúsculas/minúsculas -> ERR_NO_MATCH")
    void passwordNoCoincideMayusculas() {
        assertEquals(PasswordChecker.ERR_NO_MATCH, checker.check("Abcdef12", "abcdef12"));
    }
}
