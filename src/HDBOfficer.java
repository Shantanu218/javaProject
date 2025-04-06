import enums.ApplicationStatus;
import enums.FlatType;
import enums.RegistrationStatus;

public class HDBOfficer {
    private Project assignedProject;
    private RegistrationStatus registrationStatus;

    public boolean hasAppliedForProject(Project project) { return false; }

    public boolean registerForProject(Project project) { return false; }

    public RegistrationStatus viewRegistrationStatus() { return null; }

    public Project viewHandledProjectDetails() { return null; }

    public boolean replyToEnquiry(int enquiryID, String replyText) { return false; }

    public void bookFlat(Project project, FlatType flatType) {}

    public void updateRemainingFlats(Project project, FlatType flatType) {}

    public Application retrieveApplication(String nric) { return null; }

    public boolean updateApplicationStatus(Application application, ApplicationStatus status) { return false; }

    public boolean updateApplicantProfile(Applicant applicant, FlatType flatType) { return false; }

    public Receipt generateBookingReceipt(Application application) { return null; }
}
