import enums.ApplicationStatus;
import enums.FlatType;
import enums.RegistrationStatus;

public class Application {
    public Application(String applicationId, Applicant applicant, Project project, FlatType flatType) {
        // constructor logic
    }

    public boolean updateStatus(ApplicationStatus status) {
        // implementation
        return false;
    }

    public boolean requestWithdrawal() {
        // implementation
        return false;
    }

    public boolean isWithdrawalRequested() {
        // implementation
        return false;
    }

    public String getApplicationDetails() {
        // implementation
        return null;
    }
}
