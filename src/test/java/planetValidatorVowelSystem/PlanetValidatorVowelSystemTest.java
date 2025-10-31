package planetValidatorVowelSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanetValidatorVowelSystemTest {

    // Exemples valides donnés par l’énoncé
    private static final String TERRE   = "Terre";
    private static final String MARS    = "Mars";
    private static final String JUPITER = "Jupiter";
    private static final String URANUS  = "Uranus";
    private static final String SATURNE = "Saturne";
    private static final String NEPTUNE = "Neptune";
    private static final String MERCURE = "Mercure";
    private static final String VENUS   = "Venus";

    // Cas invalides
    private static final String INVALID_EXAMPLE = "SuperPlaneteInvalide"; // beaucoup de voyelles
    private static final String NULL_NAME = null;
    private static final String EMPTY_NAME = "";
    private static final String WITH_DIGIT = "Mars1";
    private static final String WITH_PUNCT = "Mars!";
    private static final String WITH_SPACE = "New York";
    private static final String NO_VOWEL   = "Brrr";          // aucune voyelle AEIOUY
    private static final String THREE_CONSEC_VOWELS = "Baeion"; // "aei" = 3 de suite
    private static final String SIX_VOWELS = "Baebinaiy";     // a,e,i,a,i,y => 6 voyelles (sans 3 consécutives)

    // Cas supplémentaires
    private static final String ONLY_UPPERCASE = "MARS"; // tout en majuscules
    private static final String Y_IS_VOWEL     = "Lynx"; // 'y' est une voyelle dans ce système

    private PlanetValidatorVowelSystem validator;

    @BeforeEach
    public void createValidator() {
        // Arrange
        validator = new PlanetValidatorVowelSystem();
    }

    @Test
    public void givenNullName_whenValidate_thenFalse() {
        // Act
        boolean valid = validator.isPlanetValid(NULL_NAME);
        // Assert
        assertFalse(valid);
    }

    @Test
    public void givenEmptyName_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(EMPTY_NAME);
        assertFalse(valid);
    }

    @Test
    public void givenNameWithDigit_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(WITH_DIGIT);
        assertFalse(valid);
    }

    @Test
    public void givenNameWithPunctuation_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(WITH_PUNCT);
        assertFalse(valid);
    }

    @Test
    public void givenNameWithSpace_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(WITH_SPACE);
        assertFalse(valid);
    }

    @Test
    public void givenNameWithoutAnyVowel_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(NO_VOWEL);
        assertFalse(valid);
    }

    @Test
    public void givenNameWithThreeConsecutiveVowels_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(THREE_CONSEC_VOWELS);
        assertFalse(valid);
    }

    @Test
    public void givenNameWithSixVowelsOrMore_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(SIX_VOWELS);
        assertFalse(valid);
    }

    // --- Cas valides (1 assert par planète connue) ---

    @Test
    public void givenTerre_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(TERRE));
    }

    @Test
    public void givenMars_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(MARS));
    }

    @Test
    public void givenJupiter_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(JUPITER));
    }

    @Test
    public void givenUranus_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(URANUS));
    }

    @Test
    public void givenSaturne_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(SATURNE));
    }

    @Test
    public void givenNeptune_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(NEPTUNE));
    }

    @Test
    public void givenMercure_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(MERCURE));
    }

    @Test
    public void givenVenus_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(VENUS));
    }

    // --- Autres cas positifs ciblés ---

    @Test
    public void givenUppercaseName_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(ONLY_UPPERCASE));
    }

    @Test
    public void givenNameWhereYCountsAsVowel_whenValidate_thenTrue() {
        assertTrue(validator.isPlanetValid(Y_IS_VOWEL));
    }

    // --- Exemple invalide des notes ---

    @Test
    public void givenInvalidExampleFromNotes_whenValidate_thenFalse() {
        boolean valid = validator.isPlanetValid(INVALID_EXAMPLE);
        assertFalse(valid);
    }
}
