import enums.FlatType;
import enums.MaritalStatus;

import java.util.List;
import java.util.UUID;

/**
 * Represents an Applicant in the BTO system.
 * Pure domain logic—no I/O here.
 */
public class Applicant extends User {
    private Application application;  // at most one active application

    public Applicant(String nric,
                     String password,
                     int age,
                     MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus);
    }

    /** @return the current application, or null if none. */
    public Application getApplication() {
        return application;
    }

    /**
     * Checks eligibility per the spec:
     *  - Project must be visible and within application period.
     *  - SINGLE ≥ 35 may only apply for Two_Room.
     *  - MARRIED ≥ 21 may apply for Two_Room or Three_Room.
     */
    public boolean isEligible(Project project, FlatType flatType) {
        if (!project.isVisible() || !project.isWithinApplicationPeriod()) {
            return false;
        }
        if (getMaritalStatus() == MaritalStatus.SINGLE) {
            return getAge() >= 35 && flatType == FlatType.Two_Room;
        } else { // MARRIED
            return getAge() >= 21
                && (flatType == FlatType.Two_Room
                 || flatType == FlatType.Three_Room);
        }
    }

    /**
     * Applies for the given project with the chosen flat type.
     * @throws IllegalStateException if already applied or not eligible.
     */
    public Application apply(Project project, FlatType flatType) {
        if (application != null) {
            throw new IllegalStateException("Already applied for a project");
        }
        if (!isEligible(project, flatType)) {
            throw new IllegalStateException("Not eligible for this project/flat type");
        }
        String appId = "APP-" + UUID.randomUUID();
        Application app = new Application(appId, this, project, flatType);
        project.addApplication(app);
        this.application = app;
        return app;
    }

    /**
     * Requests withdrawal of the current application.
     * @return true if the request was registered; false otherwise.
     */
    public boolean requestWithdrawal() {
        if (application == null) {
            return false;
        }
        return application.requestWithdrawal();
    }

    /**
     * Expose enquiries inherited from User.
     */
    @Override
    public List<Enquiry> getEnquiries() {
        return super.getEnquiries();
    }
}
