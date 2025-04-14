import enums.ApplicationStatus;
import enums.FlatType;
import enums.RegistrationStatus;

import java.util.Date;
import java.util.List;

public class HDBOfficer extends Applicant {
    // HDBOfficer inherits from Applicant, who holds common applicant methods and attributes.
    // Extra attributes for HDBOfficer:
    private Project assignedProject;
    private RegistrationStatus registrationStatus;

    // Constructor
    public HDBOfficer(String nric, String password, int age, String maritalStatus, List<Enquiry> enquiries) {
        super(nric, password, age, maritalStatus, enquiries);
        this.registrationStatus = RegistrationStatus.Pending; // default status
    }

    /**
     * Check if this officer has applied for the given project.
     * This might check if the officer's registration or assigned project equals the given project.
     */
    public boolean hasAppliedForProject(Project project) {
        // Example check: if assigned project is not null and equals the given project
        return assignedProject != null && assignedProject.equals(project);
    }

    /**
     * Attempt to register for a project.
     * This method should also check for constraints, e.g., not already registered in the same period.
     */
    public boolean registerForProject(Project project) {
        if (hasAppliedForProject(project)) {
            System.out.println("Already applied for this project.");
            return false;
        }
        // Verify no conflict with another registration within the same period.
        // For now, we set the assignedProject and update registrationStatus to pending.
        this.assignedProject = project;
        this.registrationStatus = RegistrationStatus.Pending;  // Registration now pending approval
        System.out.println("Registration requested for project: " + project.getProjectName());
        return true;
    }

    /**
     * Returns the current registration status for the officer.
     */
    public RegistrationStatus viewRegistrationStatus() {
        return this.registrationStatus;
    }

    /**
     * Returns the details of the project the officer is handling.
     */
    public Project viewHandledProjectDetails() {
        return this.assignedProject;
    }

    /**
     * Replies to an enquiry identified by enquiryID with the provided reply text.
     */
    public boolean replyToEnquiry(int enquiryID, String replyText) {
        // Dummy Implementation: Look up enquiry from the assigned project's enquiries list.
        List<Enquiry> enquiries = assignedProject.getEnquiries();
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryID() == enquiryID) {
                enquiry.addReply(enquiryID, replyText);
                System.out.println("Replied to enquiry ID: " + enquiryID);
                return true;
            }
        }
        System.out.println("Enquiry ID not found: " + enquiryID);
        return false;
    }

    /**
     * Books a flat by delegating to the project and updating booking details.
     */
    public void bookFlat(Project project, FlatType flatType) {
        // Check that the officer is handling the project, then update flats.
        if (assignedProject != null && assignedProject.equals(project)) {
            // Ideally, check if the flat type is available before booking.
            updateRemainingFlats(project, flatType);
            // Further operations like updating application status could be done here.
            System.out.println("Flat booked of type: " + flatType + " for project: " + project.getProjectName());
        } else {
            System.out.println("Officer is not handling the specified project.");
        }
    }

    /**
     * Update the number of remaining flats of the specified flat type in a project.
     */
    public void updateRemainingFlats(Project project, FlatType flatType) {
        // Delegate to the project to update its inventory.
        project.updateRemainingUnits(flatType, -1); // Subtract one unit
        System.out.println("Updated remaining units for flat type: " + flatType);
    }

    /**
     * Retrieves an application by applicant's NRIC. We can also do that via application ID
     */
    public Application retrieveApplication(String nric) {
        // For example, search within the assigned project's applications list.
        for (Application app : assignedProject.getApplications()) {
            if (app.getApplicant().getNric().equals(nric)) {
                return app;
            }
        }
        return null;
    }

    /**
     * Updates the status of an application.
     */
    public boolean updateApplicationStatus(Application application, ApplicationStatus status) {
        if (application != null) {
            application.updateStatus(status);
            System.out.println("Application status updated to: " + status);
            return true;
        }
        return false;
    }

    /**
     * Updates an applicant's profile with the booked flat type.
     */
    public boolean updateApplicantProfile(Applicant applicant, FlatType flatType) {
        if (applicant != null) {
            applicant.setBookedFlatType(flatType);
            System.out.println("Applicant " + applicant.getNric() + " profile updated with flat type: " + flatType);
            return true;
        }
        return false;
    }

    /**
     * Generates a receipt for the given application.
     */
    public Receipt generateBookingReceipt(Application application) {
        if (application != null) {
            Applicant app = application.getApplicant();
            String receiptID = "R-" + app.getNric() + "-" + System.currentTimeMillis();

            Receipt receipt = new Receipt(
                    receiptID,
                    app.getNric(),
                    app.getAge(),
                    app.getMaritalStatus(),
                    app.getBookedFlatType(),  // or application.getFlatTypeChosen()
                    assignedProject,
                    new Date() // or application.getApplicationDate() if needed
            );
            receipt.generateReceipt();
            System.out.println("Receipt generated for applicant: " + app.getNric());
            return receipt;
        }
        return null;
    }
}
