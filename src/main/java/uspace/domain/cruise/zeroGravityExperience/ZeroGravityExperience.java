package uspace.domain.cruise.zeroGravityExperience;

import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceAlreadyBookedException;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceFullException;

import java.util.ArrayList;
import java.util.List;

public class ZeroGravityExperience {
    private int capacity;
    private List<TravelerId> travelersBooked;

    public ZeroGravityExperience(int capacity) {
        this.capacity = capacity;
        this.travelersBooked = new ArrayList<>();
    }

    public void book(TravelerId travelerId) {
        if (hasTravelerBooked(travelerId)) {
            throw new ZeroGravityExperienceAlreadyBookedException();
        }

        if (isFull()) {
            throw new ZeroGravityExperienceFullException();
        }

        travelersBooked.add(travelerId);
    }

    public boolean hasTravelerBooked(TravelerId travelerId) {
        return travelersBooked.contains(travelerId);
    }

    private boolean isFull() {
        return travelersBooked.size() >= capacity;
    }
}
