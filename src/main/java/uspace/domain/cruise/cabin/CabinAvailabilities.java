package uspace.domain.cruise.cabin;

import uspace.domain.cruise.cabin.exceptions.CabinTypeNotAvailableException;

import java.util.Map;

public class CabinAvailabilities {
    private final Map<CabinType, Integer> availibilitiesByCabinType;

    public CabinAvailabilities(Map<CabinType, Integer> availibilitiesByCabinType) {
        this.availibilitiesByCabinType = availibilitiesByCabinType;
    }

    public int getAvailabilitiesByCabinType(CabinType cabinType) {
        return availibilitiesByCabinType.getOrDefault(cabinType, 0);
    }

    public void bookCabin(CabinType cabinType) {
        if (availibilitiesByCabinType.getOrDefault(cabinType, 0) <= 0) {
            throw new CabinTypeNotAvailableException();
        }
        availibilitiesByCabinType.put(cabinType, availibilitiesByCabinType.get(cabinType) - 1);
    }
}
