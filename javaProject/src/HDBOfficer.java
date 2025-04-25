import enums.FlatType;
import enums.RegistrationStatus;
import enums.ApplicationStatus;
import enums.MaritalStatus;

/**
 * Represents an HDB Officer.
 * Inherits Applicant capabilities and adds:
 *  - registering to handle a project
 *  - replying to enquiries
 *  - booking flats for successful applications
 */
public class HDBOfficer extends Applicant {
    private Project handledProject;
    private RegistrationStatus registrationStatus = RegistrationStatus.Pending;

    public HDBOfficer(String nric,
                      String password,
                      int age,
                      MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus);
    }

    /**
     * Request to register for a project (pending manager approval).
     * @throws IllegalStateException if already requested or outside period.
     */
    public void registerForProject(Project project) {
        if (handledProject != null && handledProject.equals(project)) {
            throw new IllegalStateException("Already requested registration for project " + project.getProjectID());
        }
        if (!project.isWithinApplicationPeriod()) {
            throw new IllegalStateException("Cannot register outside application period");
        }
        this.handledProject     = project;
        this.registrationStatus = RegistrationStatus.Pending;
    }

    /** Approve this officer’s registration (called by HDBManager). */
    public void approveRegistration() {
        if (registrationStatus != RegistrationStatus.Pending) {
            throw new IllegalStateException("No pending registration to approve");
        }
        this.registrationStatus = RegistrationStatus.Approved;
    }

    /** Reject this officer’s registration (called by HDBManager). */
    public void rejectRegistration() {
        if (registrationStatus != RegistrationStatus.Pending) {
            throw new IllegalStateException("No pending registration to reject");
        }
        this.registrationStatus = RegistrationStatus.Rejected;
        this.handledProject     = null;
    }

    /** @return the current registration status. */
    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    /** @return the project this officer is approved to handle (or null). */
    public Project getHandledProject() {
        return handledProject;
    }

    /**
     * Reply to an enquiry on the handled project.
     * @throws IllegalArgumentException if enquiry not found.
     */
    public void replyToEnquiry(Enquiry enquiry, String replyText) {
        if (handledProject == null || !handledProject.getEnquiries().contains(enquiry)) {
            throw new IllegalArgumentException("Enquiry not found on handled project");
        }
        enquiry.reply(replyText, this);
    }

    /**
     * Book a flat for a successful application.
     * Marks application BOOKED and decrements the project’s available units.
     * @throws IllegalStateException if not assigned or application not successful.
     */
    public void bookFlat(Application application) {
        if (handledProject == null || !handledProject.equals(application.getProject())) {
            throw new IllegalStateException("Officer not assigned to this project");
        }
        if (application.getStatus() != ApplicationStatus.SUCCESSFUL) {
            throw new IllegalStateException("Cannot book: application status is " + application.getStatus());
        }
        application.book();  // transitions status to Booked
        handledProject.decrementUnits(application.getFlatTypeChosen());
    }
}
