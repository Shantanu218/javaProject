import enums.FlatType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Build-To-Order (BTO) project listing.
 */
public class Project {
    private final String projectID;
    private String projectName;
    private String neighborhood;
    private int twoRoomUnits;
    private int threeRoomUnits;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private boolean visible;
    private HDBManager managerInCharge;
    private int availableOfficerSlots;

    private final List<Enquiry> enquiries = new ArrayList<>();
    private final List<Application> applications = new ArrayList<>();
    private final List<OfficerRegistration> officerRegistrations = new ArrayList<>();

    public Project(String projectID,
                   String projectName,
                   String neighborhood,
                   int twoRoomUnits,
                   int threeRoomUnits,
                   LocalDate openingDate,
                   LocalDate closingDate,
                   HDBManager managerInCharge,
                   int availableOfficerSlots) {
        this.projectID             = Objects.requireNonNull(projectID);
        this.projectName           = Objects.requireNonNull(projectName);
        this.neighborhood          = Objects.requireNonNull(neighborhood);
        this.twoRoomUnits          = twoRoomUnits;
        this.threeRoomUnits        = threeRoomUnits;
        this.applicationOpeningDate= Objects.requireNonNull(openingDate);
        this.applicationClosingDate= Objects.requireNonNull(closingDate);
        this.visible               = true;
        this.managerInCharge       = Objects.requireNonNull(managerInCharge);
        this.availableOfficerSlots = availableOfficerSlots;
    }

    /** Toggle whether applicants see this project. */
    public void toggleVisibility() {
        this.visible = !this.visible;
    }

    public boolean isVisible() {
        return visible;
    }

    /** Within the start–end application window? */
    public boolean isWithinApplicationPeriod() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(applicationOpeningDate) || now.isAfter(applicationOpeningDate))
            && (now.isEqual(applicationClosingDate) || now.isBefore(applicationClosingDate));
    }

    /** Add a new enquiry. */
    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    /** Add a new application. */
    public void addApplication(Application application) {
        applications.add(application);
    }

    /** Add a new officer registration. */
    public void addOfficerRegistration(OfficerRegistration reg) {
        officerRegistrations.add(reg);
    }

    /** Called when an application is approved/booked to decrement stock. */
    public void decrementUnits(FlatType flatType) {
        switch (flatType) {
            case Two_Room  -> twoRoomUnits = Math.max(0, twoRoomUnits - 1);
            case Three_Room-> threeRoomUnits = Math.max(0, threeRoomUnits - 1);
        }
    }

    /** Called when a booked application is withdrawn to return a unit. */
    public void incrementUnits(FlatType flatType) {
        switch (flatType) {
            case Two_Room  -> twoRoomUnits++;
            case Three_Room-> threeRoomUnits++;
        }
    }

    /** Occupy one officer slot (after manager approval). */
    public void occupyOfficerSlot() {
        if (availableOfficerSlots <= 0) {
            throw new IllegalStateException("No HDB officer slots remaining");
        }
        availableOfficerSlots--;
    }

    // ─── Getters & setters ──────────────────────────────────────────────────

    public String getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = Objects.requireNonNull(projectName);
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = Objects.requireNonNull(neighborhood);
    }

    public int getTwoRoomUnits() {
        return twoRoomUnits;
    }

    public void setTwoRoomUnits(int twoRoomUnits) {
        this.twoRoomUnits = twoRoomUnits;
    }

    public int getThreeRoomUnits() {
        return threeRoomUnits;
    }

    public void setThreeRoomUnits(int threeRoomUnits) {
        this.threeRoomUnits = threeRoomUnits;
    }

    public LocalDate getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
        this.applicationOpeningDate = Objects.requireNonNull(applicationOpeningDate);
    }

    public LocalDate getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public void setApplicationClosingDate(LocalDate applicationClosingDate) {
        this.applicationClosingDate = Objects.requireNonNull(applicationClosingDate);
    }

    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    public void setManagerInCharge(HDBManager manager) {
        this.managerInCharge = Objects.requireNonNull(manager);
    }

    public int getAvailableOfficerSlots() {
        return availableOfficerSlots;
    }

    public void setAvailableOfficerSlots(int availableOfficerSlots) {
        this.availableOfficerSlots = availableOfficerSlots;
    }

    public List<Enquiry> getEnquiries() {
        return List.copyOf(enquiries);
    }

    public List<Application> getApplications() {
        return List.copyOf(applications);
    }

    public List<OfficerRegistration> getOfficerRegistrations() {
        return List.copyOf(officerRegistrations);
    }

    @Override
    public String toString() {
        return String.format(
            "%s: %s [%s] 2-Room:%d 3-Room:%d Visible:%b Slots:%d",
            projectID, projectName, neighborhood,
            twoRoomUnits, threeRoomUnits, visible, availableOfficerSlots
        );
    }
}
