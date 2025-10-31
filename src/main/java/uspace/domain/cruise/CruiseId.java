package uspace.domain.cruise;

public class CruiseId {
    private final String id;

    public CruiseId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        CruiseId other = (CruiseId) obj;
        return id.equals(other.id);
    }
}
