import enums.RegistrationStatus;

public class OfficerRegistration {
    private static int nextId = 1;

    private final int id;
    private final Project project;
    private final User officer;
    private RegistrationStatus status;

    public OfficerRegistration(Project project, User officer) {
        this.id      = nextId++;
        this.project = project;
        this.officer = officer;
        this.status  = RegistrationStatus.Pending;  // use your enum’s Pending
    }

    public int getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public User getOfficer() {
        return officer;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    /** Now accepts your enum type */
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "Reg[%d] Officer:%s Project:%s Status:%s",
            id, officer.getNric(), project.getProjectID(), status
        );
    }
}
