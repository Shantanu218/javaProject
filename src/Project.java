import enums.FlatType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private final String projectID;
    private final String projectName;
    private final String neighborhood;
    private final String flatType;
    private int twoRoomUnits;
    private int threeRoomUnits;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private boolean visibility;
    private HDBManager managerInCharge;
    private int availableOfficerSlot;
    private List<Enquiry> enquiries;
    private List<Application> applications;
    private List<Registration> officerRegistrations;

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
        this.visibility = true;
        this.enquiries = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.officerRegistrations = new ArrayList<>();
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isWithinApplicationPeriod() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(applicationOpeningDate) || now.isAfter(applicationOpeningDate)) &&
                (now.isEqual(applicationClosingDate) || now.isBefore(applicationClosingDate));
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    public void addApplication(Application application) {
        applications.add(application);
    }

    public void addOfficerRegistration(Registration registration) {
        officerRegistrations.add(registration);
    }

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

    // Getters (if needed)

    public String getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getFlatType() {
        return flatType;
    }

    public int getTwoRoomUnits() {
        return twoRoomUnits;
    }

    public int getThreeRoomUnits() {
        return threeRoomUnits;
    }

    public LocalDate getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public LocalDate getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    public int getAvailableOfficerSlot() {
        return availableOfficerSlot;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public List<Registration> getOfficerRegistrations() {
        return officerRegistrations;
    }

    public void decreaseOfficerSlot() {
        if (availableOfficerSlot > 0) {
            availableOfficerSlot--;
        }
    }
}