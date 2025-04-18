import enums.FlatType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * HDBManager is responsible for managing BTO project listings and handling HDB Officer registration requests.
 * <p>
 * This implementation covers methods for project management (create, update, delete, toggle visibility, view projects)
 * and for processing officer registrations (view, approve, reject registrations).
 * The methods related to application approval, withdrawal, report generation, and enquiry replies are declared as placeholders.
 */
public class HDBManager extends User {

    // Global lists simulating the system-wide storage of projects and officer registrations.
    private static List<Project> allProjects = new ArrayList<>();
    private static List<OfficerRegistration> allOfficerRegistrations = new ArrayList<>();

    /**
     * Constructs an HDBManager.
     *
     * @param nric          the NRIC of the manager
     * @param password      the manager's password
     * @param age           the manager's age
     * @param maritalStatus the manager's marital status
     * @param enquiries     the list of enquiries (if null, will be set to an empty list)
     */
    public HDBManager(String nric, String password, int age, String maritalStatus, List<Enquiry> enquiries) {
        super(nric, password, age, maritalStatus, enquiries);
    }

    /* =====================================================
       PROJECT MANAGEMENT METHODS
       ===================================================== */

    /**
     * Creates a new project (BTO project listing) and adds it to the system.
     * The project is tagged with this manager as its creator.
     *
     * @param project the new Project to create
     * @return the updated list of all projects
     */
    public List<Project> createProject(Project project) {
        project.setCreatedBy(this); // Uses the Project method: setCreatedBy(HDBManager)
        allProjects.add(project);
        return allProjects;
    }

    /**
     * Updates an existing project with new details.
     * Only projects created by this manager can be updated.
     *
     * @param oldProject     the existing project to update
     * @param updatedProject a Project object containing the updated details
     */
    public void updateProject(Project oldProject, Project updatedProject) {
        if (!allProjects.contains(oldProject)) {
            System.out.println("Update failed: Project not found.");
            return;
        }
        if (oldProject.getManagerInCharge() != this) { // Instead of getCreatedBy()
            System.out.println("Update failed: You did not create this project.");
            return;
        }
        oldProject.setProjectName(updatedProject.getProjectName());
        oldProject.setNeighborhood(updatedProject.getNeighborhood());
        oldProject.setTwoRoomUnits(updatedProject.getTwoRoomUnits());
        oldProject.setThreeRoomUnits(updatedProject.getThreeRoomUnits());
        oldProject.setApplicationOpeningDate(updatedProject.getApplicationOpeningDate());
        oldProject.setApplicationClosingDate(updatedProject.getApplicationClosingDate());
    }

    /**
     * Deletes a project from the system.
     * Only projects created by this manager can be deleted.
     *
     * @param project the project to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProject(Project project) {
        if (!allProjects.contains(project)) {
            System.out.println("Delete failed: Project not found.");
            return false;
        }
        if (project.getManagerInCharge() != this) {
            System.out.println("Delete failed: You did not create this project.");
            return false;
        }
        allProjects.remove(project);
        return true;
    }

    /**
     * Toggles the visibility of a project.
     *
     * @param project    the project whose visibility is to be toggled
     * @param visibility true for visible, false for hidden
     * @return true if toggled successfully, false otherwise
     */
    public boolean toggleProjectVisibility(Project project, boolean visibility) {
        if (!allProjects.contains(project)) {
            System.out.println("Toggle failed: Project not found.");
            return false;
        }
        if (project.getManagerInCharge() != this) {
            System.out.println("Toggle failed: You did not create this project.");
            return false;
        }
        project.setVisibility(visibility);  // Use setVisibility(boolean) method
        return true;
    }

    /**
     * Retrieves a list of all projects in the system.
     *
     * @return a list of all projects
     */
    public List<Project> viewAllProjects() {
        return new ArrayList<>(allProjects);
    }

    /**
     * Retrieves a list of projects created by this manager.
     *
     * @return a list of projects this manager created
     */
    public List<Project> viewCreatedProjects() {
        List<Project> createdProjects = new ArrayList<>();
        for (Project p : allProjects) {
            if (p.getManagerInCharge() == this) {
                createdProjects.add(p);
            }
        }
        return createdProjects;
    }

    /* =============================================================
       OFFICER REGISTRATION MANAGEMENT METHODS
       ============================================================= */

    /**
     * Retrieves all officer registrations for a given project.
     *
     * @param project the project to check for registrations
     * @return a list of OfficerRegistration objects associated with that project
     */
    public List<OfficerRegistration> viewOfficerRegistrations(Project project) {
        List<OfficerRegistration> registrations = new ArrayList<>();
        for (OfficerRegistration reg : allOfficerRegistrations) {
            if (reg.getProject() == project) {
                registrations.add(reg);
            }
        }
        return registrations;
    }

    /**
     * Approves an officer registration for a project.
     * The registration must belong to a project created by this manager.
     *
     * @param registration the officer registration to approve
     */
    public void approveOfficerRegistration(OfficerRegistration registration) {
        if (!allOfficerRegistrations.contains(registration)) {
            System.out.println("Approval failed: Registration not found.");
            return;
        }
        if (registration.getProject().getManagerInCharge() != this) {
            System.out.println("Approval failed: Not your project.");
            return;
        }
        registration.setStatus("Approved");
        // Optionally: Decrease available officer slots in the project.
    }

    /**
     * Rejects an officer registration for a project.
     * The registration must belong to a project created by this manager.
     *
     * @param registration the officer registration to reject
     */
    public void rejectOfficerRegistration(OfficerRegistration registration) {
        if (!allOfficerRegistrations.contains(registration)) {
            System.out.println("Rejection failed: Registration not found.");
            return;
        }
        if (registration.getProject().getManagerInCharge() != this) {
            System.out.println("Rejection failed: Not your project.");
            return;
        }
        registration.setStatus("Rejected");
    }

    /* ======================================================
       APPLICATION / WITHDRAWAL / REPORT / ENQUIRY METHODS
       ====================================================== */

    public boolean approveApplication(Application application) {
        // TODO: To be implemented
        return false;
    }

    public boolean rejectApplication(Application application) {
        // TODO: To be implemented
        return false;
    }

    public boolean approveWithdrawal(Application application) {
        // TODO: To be implemented
        return false;
    }

    public boolean rejectWithdrawal(Application application) {
        // TODO: To be implemented
        return false;
    }

    public String generateFlatBookingReport(List<Application> applications) {
        // TODO: To be implemented
        return "";
    }

    public List<Enquiry> viewAllEnquiries() {
        // TODO: To be implemented
        return new ArrayList<>();
    }

    public boolean replyToEnquiry(Enquiry enquiry, String replyText) {
        // TODO: To be implemented
        return false;
    }

    /* =======================================================
       DEMO MAIN METHOD (Optional) -- For testing purposes
       ======================================================= */
       public static void main(String[] args) {
        // Create an HDBManager instance.
        HDBManager manager = new HDBManager("S1234567A", "password", 40, "Married", new ArrayList<>());

        // Create a sample project using the correct constructor parameters.
        Project project = new Project(
                "P001", 
                "Sunrise Ville", 
                "Boon Lay", 
                "2-Room", 
                100, 
                50, 
                LocalDate.now().minusDays(1), 
                LocalDate.now().plusDays(30), 
                manager, 
                10
        );
        manager.createProject(project);
        System.out.println("Project Created: " + project.getProjectName());

        // Update the project details.
        Project updatedProject = new Project(
                "P001", 
                "Sunrise Ville Updated", 
                "Jurong East", 
                "2-Room", 
                90, 
                45, 
                LocalDate.now().minusDays(1), 
                LocalDate.now().plusDays(30), 
                manager, 
                10
        );
        manager.updateProject(project, updatedProject);
        System.out.println("Updated Project: " + project.getProjectName() + ", Neighborhood: " + project.getNeighborhood());

        // Toggle project visibility.
        manager.toggleProjectVisibility(project, false);
        System.out.println("Project visibility (should be false): " + project.isVisible());

        // Simulate officer registrations using a dummy officer.
        User dummyOfficer = new User("T2345678B", "password", 35, "Single", new ArrayList<>()) {};
        OfficerRegistration reg1 = new OfficerRegistration(project, dummyOfficer);
        allOfficerRegistrations.add(reg1);
        manager.approveOfficerRegistration(reg1);
        System.out.println("Officer Registration Status after Approval: " + reg1.getStatus());

        OfficerRegistration reg2 = new OfficerRegistration(project, dummyOfficer);
        allOfficerRegistrations.add(reg2);
        manager.rejectOfficerRegistration(reg2);
        System.out.println("Officer Registration Status after Rejection: " + reg2.getStatus());

        // Create a dummy applicant for testing the Application.
        Applicant dummyApplicant = new Applicant("S7654321B", "password", 30, "Single", new ArrayList<>());
        // Use the enum value for flat type, e.g., FlatType.Two_Room (assume this enum value exists).
        Application dummyApp = new Application("A001", dummyApplicant, project, FlatType.Two_Room);
        // Call the stub method (teammate's part) to process the application.
        manager.approveApplication(dummyApp);
    }
}
