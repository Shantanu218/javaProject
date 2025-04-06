import java.util.List;
import enums.ApplicationStatus;
import enums.FlatType;

public class Applicant extends User {
    private Project appliedProject;
    private String applicationStatus;
    private FlatType bookedFlatType;

    public List<Project> viewOpenProjects() {
        // implementation
        return null;
    }

    public boolean applyForProject(Project project) {
        // implementation
        return false;
    }

    public ApplicationStatus viewApplicationStatus() {
        // implementation
        return null;
    }

    public boolean requestWithdrawal() {
        // implementation
        return false;
    }

    public Enquiry submitEnquiry(Project project, String enquiryText) {
        // implementation
        return null;
    }

    public boolean editEnquiry(int enquiryID, String newText) {
        // implementation
        return false;
    }

    public boolean deleteEnquiry(int enquiryID) {
        // implementation
        return false;
    }
}
