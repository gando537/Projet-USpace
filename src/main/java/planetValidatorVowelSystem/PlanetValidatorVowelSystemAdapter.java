package planetValidatorVowelSystem;

import uspace.application.cruise.planetvalidation.PlanetValidator;

public class PlanetValidatorVowelSystemAdapter implements PlanetValidator {

    private final PlanetValidatorVowelSystem delegate = new PlanetValidatorVowelSystem();

    @Override
    public boolean isValid(String planetName) {
        return delegate.isPlanetValid(planetName);
    }
}
