import enums.ApplicationStatus;
import enums.FlatType;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an Applicant’s BTO application.
 */
public class Application {
    private final String applicationID;
    private final Applicant applicant;
    private final Project project;
    private final LocalDate applicationDate;

    private ApplicationStatus status;
    private final FlatType flatTypeChosen;
    private boolean withdrawalRequested;

    public Application(String applicationID,
                       Applicant applicant,
                       Project project,
                       FlatType flatTypeChosen) {
        this.applicationID      = Objects.requireNonNull(applicationID);
        this.applicant          = Objects.requireNonNull(applicant);
        this.project            = Objects.requireNonNull(project);
        this.flatTypeChosen     = Objects.requireNonNull(flatTypeChosen);
        this.applicationDate    = LocalDate.now();
        this.status             = ApplicationStatus.PENDING;
        this.withdrawalRequested = false;
    }

    // ─── Domain actions ────────────────────────────────────────────────────────

    /** Approve this application if units available; else UNSUCCESSFUL. */
    public void approve() {
        if (status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only approve a PENDING application");
        }
        boolean success = false;
        switch (flatTypeChosen) {
            case Two_Room:
                if (project.getTwoRoomUnits() > 0) {
                    project.decrementUnits(flatTypeChosen);
                    success = true;
                }
                break;
            case Three_Room:
                if (project.getThreeRoomUnits() > 0) {
                    project.decrementUnits(flatTypeChosen);
                    success = true;
                }
                break;
        }
        status = success
               ? ApplicationStatus.SUCCESSFUL
               : ApplicationStatus.UNSUCCESSFUL;
    }

    /** Reject this application (mark UNSUCCESSFUL). */
    public void reject() {
        if (status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only reject a PENDING application");
        }
        status = ApplicationStatus.UNSUCCESSFUL;
    }

    /** Book the flat (after a SUCCESSFUL approval). */
    public void book() {
        if (status != ApplicationStatus.SUCCESSFUL) {
            throw new IllegalStateException("Can only book a SUCCESSFUL application");
        }
        status = ApplicationStatus.BOOKED;
    }

    /** Withdraw the application (after request). Rolls back inventory if already booked. */
    public void withdraw() {
        if (!withdrawalRequested) {
            throw new IllegalStateException("Withdrawal must be requested first");
        }
        // rollback units if previously BOOKED
        if (status == ApplicationStatus.BOOKED) {
            project.incrementUnits(flatTypeChosen);
        }
        status = ApplicationStatus.WITHDRAWN;
    }

    // ─── Withdrawal request ───────────────────────────────────────────────────

    /**
     * Applicant flags their intent to withdraw.
     * @return true if this is the first request; false if already requested.
     */
    public boolean requestWithdrawal() {
        if (withdrawalRequested || status == ApplicationStatus.WITHDRAWN) {
            return false;
        }
        withdrawalRequested = true;
        return true;
    }

    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    // ─── Read‐only view ────────────────────────────────────────────────────────

    public String getApplicationDetails() {
        return String.format(
            "AppID: %s | Project: %s | Flat: %s | Status: %s",
            applicationID,
            project.getProjectName(),
            flatTypeChosen,
            status
        );
    }

    // ─── Getters ───────────────────────────────────────────────────────────────

    public String getApplicationID()       { return applicationID; }
    public Applicant getApplicant()        { return applicant; }
    public Project getProject()            { return project; }
    public LocalDate getApplicationDate()  { return applicationDate; }
    public ApplicationStatus getStatus()   { return status; }
    public FlatType getFlatTypeChosen()    { return flatTypeChosen; }

    @Override
    public String toString() {
        return getApplicationDetails();
    }
}
