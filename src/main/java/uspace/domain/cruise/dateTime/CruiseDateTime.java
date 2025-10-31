package uspace.domain.cruise.dateTime;

import java.time.LocalDateTime;

public class CruiseDateTime {
    private final LocalDateTime localDateTime;

    public CruiseDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public boolean isBefore(CruiseDateTime otherCruiseDateTime) {
        return localDateTime.isBefore(otherCruiseDateTime.localDateTime);
    }

    public boolean isAfter(CruiseDateTime otherCruiseDateTime) {
        return localDateTime.isAfter(otherCruiseDateTime.localDateTime);
    }

    public CruiseDateTime plusDays(long days) {
        return new CruiseDateTime(localDateTime.plusDays(days));
    }

    @Override
    public String toString() {
        return localDateTime.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CruiseDateTime that = (CruiseDateTime) obj;
        return localDateTime.equals(that.localDateTime);
    }

    @Override
    public int hashCode() {
        return localDateTime.hashCode();
    }
}
