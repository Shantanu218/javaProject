import enums.FlatType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Build-To-Order (BTO) project listing.
 * Contains information about the project details, application period, 
 * available flat units, the manager in charge, available officer slots,
 * enquiries, applications, and officer registrations.
 */
public class Project {
    private final String projectID; // Unique identifier, immutable
    private String projectName;
    private String neighborhood;
    private String flatType;
    private int twoRoomUnits;
    private int threeRoomUnits;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private boolean visibility;
    private HDBManager managerInCharge;
    private int availableOfficerSlot;
    private List<Enquiry> enquiries;
    private List<Application> applications;
    private List<OfficerRegistration> officerRegistrations;

    /**
     * Constructs a new Project with the given details.
     *
     * @param projectID             the unique identifier for the project
     * @param projectName           the project's name
     * @param neighborhood          the neighborhood for the project
     * @param flatType              the type of flat (e.g., "2-Room" or "3-Room")
     * @param twoRoomUnits          the number of 2-Room units available
     * @param threeRoomUnits        the number of 3-Room units available
     * @param openingDate           the application opening date
     * @param closingDate           the application closing date
     * @param managerInCharge       the HDBManager responsible for this project
     * @param availableOfficerSlot  the number of HDB officer slots available
     */
    public Project(String projectID, String projectName, String neighborhood, String flatType,
                   int twoRoomUnits, int threeRoomUnits, LocalDate openingDate, LocalDate closingDate,
                   HDBManager managerInCharge, int availableOfficerSlot) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.flatType = flatType;
        this.twoRoomUnits = twoRoomUnits;
        this.threeRoomUnits = threeRoomUnits;
        this.applicationOpeningDate = openingDate;
        this.applicationClosingDate = closingDate;
        this.managerInCharge = managerInCharge;
        this.availableOfficerSlot = availableOfficerSlot;
        this.visibility = true; // Default visibility is on.
        this.enquiries = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.officerRegistrations = new ArrayList<>();
    }

    /**
     * Checks if the project is currently visible.
     *
     * @return true if visible; false otherwise.
     */
    public boolean isVisible() {
        return visibility;
    }

    /**
     * Sets the project's visibility.
     *
     * @param visibility true to make visible; false to hide.
     */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    /**
     * Determines whether the current date is within the application period.
     *
     * @return true if within period; false otherwise.
     */
    public boolean isWithinApplicationPeriod() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(applicationOpeningDate) || now.isAfter(applicationOpeningDate)) &&
                (now.isEqual(applicationClosingDate) || now.isBefore(applicationClosingDate));
    }

    /**
     * Adds an enquiry to the project.
     *
     * @param enquiry the enquiry to add.
     */
    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    /**
     * Adds an application to the project.
     *
     * @param application the application to add.
     */
    public void addApplication(Application application) {
        applications.add(application);
    }

    /**
     * Adds an officer registration to the project.
     *
     * @param registration the officer registration to add.
     */
    public void addOfficerRegistration(OfficerRegistration registration) {
        officerRegistrations.add(registration);
    }

    /**
     * Updates the remaining flat units for a given flat type.
     *
     * @param flatType the flat type to update.
     * @param delta    the change in unit count (positive or negative).
     */
    public void updateRemainingUnits(FlatType flatType, int delta) {
        switch (flatType) {
            case Two_Room:
                twoRoomUnits = Math.max(0, twoRoomUnits + delta);
                break;
            case Three_Room:
                threeRoomUnits = Math.max(0, threeRoomUnits + delta);
                break;
        }
    }

    // Getters and setters for updatable fields:

    public String getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
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
        this.applicationOpeningDate = applicationOpeningDate;
    }

    public LocalDate getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public void setApplicationClosingDate(LocalDate applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    /**
     * Sets the manager in charge (i.e., the creator) of this project.
     *
     * @param manager the HDBManager to set as creator.
     */
    public void setCreatedBy(HDBManager manager) {
        this.managerInCharge = manager;
    }

    public int getAvailableOfficerSlot() {
        return availableOfficerSlot;
    }

    public void setAvailableOfficerSlot(int availableOfficerSlot) {
        this.availableOfficerSlot = availableOfficerSlot;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public List<OfficerRegistration> getOfficerRegistrations() {
        return officerRegistrations;
    }

    /**
     * Decreases the available officer slot count by one, if possible.
     */
    public void decreaseOfficerSlot() {
        if (availableOfficerSlot > 0) {
            availableOfficerSlot--;
        }
    }
}