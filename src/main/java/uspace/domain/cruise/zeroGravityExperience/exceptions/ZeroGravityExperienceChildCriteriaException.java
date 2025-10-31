package uspace.domain.cruise.zeroGravityExperience.exceptions;

public class ZeroGravityExperienceChildCriteriaException extends RuntimeException {
    public ZeroGravityExperienceChildCriteriaException() {
        super("Child traveler criteria not satisfied for Zero-G experience");
    }
}
