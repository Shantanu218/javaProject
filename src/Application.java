import enums.ApplicationStatus;
import enums.FlatType;
import enums.RegistrationStatus;

import java.time.LocalDate;

public class Application {
    private String applicationID;
    private Applicant applicant;
    private Project project;
    private LocalDate applicationDate;
    private ApplicationStatus status;
    private FlatType flatTypeChosen;
    private boolean withdrawalRequest;


    public Application(String applicationId, Applicant applicant, Project project, FlatType flatType) {
        this.applicationID = applicationId;
        this.applicant = applicant;
        this.project = project;
        this.flatTypeChosen = flatType;
        this.applicationDate = LocalDate.now(); // sets to current date
        this.status = ApplicationStatus.Pending; // default status on creation
        this.withdrawalRequest = false; // default to no withdrawal request
    }

    public void updateStatus(ApplicationStatus stats) {
        status = stats;
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

    // getter and setter methods
    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public FlatType getFlatTypeChosen() {
        return flatTypeChosen;
    }

    public void setFlatTypeChosen(FlatType flatTypeChosen) {
        this.flatTypeChosen = flatTypeChosen;
    }

    public boolean isWithdrawalRequest() {
        return withdrawalRequest;
    }

    public void setWithdrawalRequest(boolean withdrawalRequest) {
        this.withdrawalRequest = withdrawalRequest;
    }
}
