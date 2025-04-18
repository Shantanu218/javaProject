public class OfficerRegistration {
    private Project project;
    private User officer; // or HDBOfficer if preferred
    private String status; // e.g., "Pending", "Approved", "Rejected"

    public OfficerRegistration(Project project, User officer) {
        this.project = project;
        this.officer = officer;
        this.status = "Pending";
    }

    public Project getProject() {
        return project;
    }
    
    public User getOfficer() {
        return officer;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
