import enums.ApplicationStatus;
import enums.FlatType;
import enums.RegistrationStatus;
import enums.MaritalStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an HDB Manager who can:
 *  - create/update/delete projects (one per period)
 *  - toggle project visibility
 *  - view all or their own projects
 *  - approve/reject officer registrations (respecting slot limits)
 *  - approve/reject BTO applications
 *  - process withdrawal requests
 *  - generate a flat‐booking report
 */
public class HDBManager extends User {
    private final List<Project> projectRepo;
    private final List<OfficerRegistration> officerRegRepo;

    /**
     * Full constructor (used by Main wiring).
     */
    public HDBManager(
        String nric,
        String password,
        int age,
        MaritalStatus maritalStatus,
        List<Project> projectRepo,
        List<OfficerRegistration> officerRegRepo
    ) {
        super(nric, password, age, maritalStatus);
        this.projectRepo    = projectRepo;
        this.officerRegRepo = officerRegRepo;
    }

    /**
     * Convenience constructor for data‐loading (no repos yet).
     */
    public HDBManager(String nric, String password, int age, MaritalStatus maritalStatus) {
        this(nric, password, age, maritalStatus,
             new ArrayList<>(),
             new ArrayList<>()
        );
    }

    // --- PROJECT MANAGEMENT ---

    public Project createProject(Project p) {
        validateNoOverlap(p);
        projectRepo.add(p);
        return p;
    }

    public Project updateProject(String projectID, Project updated) {
        Project existing = findProject(projectID);
        if (!existing.getManagerInCharge().equals(this)) {
            throw new IllegalStateException("Unauthorized to edit this project");
        }
        existing.setProjectName(updated.getProjectName());
        existing.setNeighborhood(updated.getNeighborhood());
        existing.setApplicationOpeningDate(updated.getApplicationOpeningDate());
        existing.setApplicationClosingDate(updated.getApplicationClosingDate());
        existing.setTwoRoomUnits(updated.getTwoRoomUnits());
        existing.setThreeRoomUnits(updated.getThreeRoomUnits());
        return existing;
    }

    public boolean deleteProject(String projectID) {
        Project p = findProject(projectID);
        if (!p.getManagerInCharge().equals(this)) return false;
        return projectRepo.remove(p);
    }

    public void toggleProjectVisibility(String projectID) {
        Project p = findProject(projectID);
        if (!p.getManagerInCharge().equals(this)) {
            throw new IllegalStateException("Unauthorized to toggle visibility");
        }
        p.toggleVisibility();
    }

    public List<Project> viewAllProjects() {
        return Collections.unmodifiableList(projectRepo);
    }

    public List<Project> viewMyProjects() {
        List<Project> mine = new ArrayList<>();
        for (Project p : projectRepo) {
            if (p.getManagerInCharge().equals(this)) {
                mine.add(p);
            }
        }
        return Collections.unmodifiableList(mine);
    }

    private Project findProject(String projectID) {
        return projectRepo.stream()
            .filter(p -> p.getProjectID().equals(projectID))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectID));
    }

    private void validateNoOverlap(Project newProject) {
        for (Project existing : viewMyProjects()) {
            if (datesOverlap(existing, newProject)) {
                throw new IllegalStateException("Project application period overlaps an existing project");
            }
        }
    }

    private boolean datesOverlap(Project a, Project b) {
        LocalDate aStart = a.getApplicationOpeningDate();
        LocalDate aEnd   = a.getApplicationClosingDate();
        LocalDate bStart = b.getApplicationOpeningDate();
        LocalDate bEnd   = b.getApplicationClosingDate();
        return !aEnd.isBefore(bStart) && !bEnd.isBefore(aStart);
    }

    // --- OFFICER REGISTRATION MANAGEMENT ---

    public OfficerRegistration approveOfficerRegistration(int registrationID) {
        OfficerRegistration reg = findRegistration(registrationID);
        if (!reg.getProject().getManagerInCharge().equals(this)) {
            throw new IllegalStateException("Unauthorized to approve this registration");
        }
        reg.setStatus(RegistrationStatus.Approved);
        reg.getProject().occupyOfficerSlot();
        return reg;
    }

    public OfficerRegistration rejectOfficerRegistration(int registrationID) {
        OfficerRegistration reg = findRegistration(registrationID);
        if (!reg.getProject().getManagerInCharge().equals(this)) {
            throw new IllegalStateException("Unauthorized to reject this registration");
        }
        reg.setStatus(RegistrationStatus.Rejected);
        return reg;
    }

    private OfficerRegistration findRegistration(int id) {
        return officerRegRepo.stream()
            .filter(r -> r.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Registration not found: " + id));
    }

    // --- APPLICATION APPROVAL & REJECTION ---

    public Application approveApplication(String applicationID) {
        Application app = findApplication(applicationID);
        app.approve();
        return app;
    }

    public Application rejectApplication(String applicationID) {
        Application app = findApplication(applicationID);
        app.reject();
        return app;
    }

    private Application findApplication(String applicationID) {
        return projectRepo.stream()
            .flatMap(p -> p.getApplications().stream())
            .filter(a -> a.getApplicationID().equals(applicationID))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationID));
    }

    // --- WITHDRAWAL MANAGEMENT ---

    public Application processWithdrawal(String applicationID) {
        Application app = findApplication(applicationID);
        app.withdraw();
        return app;
    }

    // --- REPORT GENERATION ---

    public String generateFlatBookingReport() {
        StringBuilder sb = new StringBuilder();
        projectRepo.stream()
            .flatMap(p -> p.getApplications().stream())
            .filter(a -> a.getStatus() == ApplicationStatus.BOOKED)
            .forEach(a -> sb.append(a).append("\n"));
        return sb.toString();
    }
}
