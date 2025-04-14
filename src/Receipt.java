import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import enums.FlatType;

public class Receipt {
    private String receiptID;
    private String applicantNRIC;
    private int applicantAge;
    private String maritalStatus;
    private FlatType flatTypeBooked;
    private Project project;
    private Date bookingDate;

    public Receipt(String receiptID,String applicantNRIC, int applicantAge,
                   String maritalStatus, FlatType flatTypeBooked, Project project, Date bookingDate) {
        this.receiptID = receiptID;
        this.applicantNRIC = applicantNRIC;
        this.applicantAge = applicantAge;
        this.maritalStatus = maritalStatus;
        this.flatTypeBooked = flatTypeBooked;
        this.project = project;
        this.bookingDate = bookingDate;
    }

    // Getters and setters

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
    }

    public int getApplicantAge() {
        return applicantAge;
    }

    public void setApplicantAge(int applicantAge) {
        this.applicantAge = applicantAge;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public FlatType getFlatTypeBooked() {
        return flatTypeBooked;
    }

    public void setFlatTypeBooked(FlatType flatTypeBooked) {
        this.flatTypeBooked = flatTypeBooked;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    // Method to generate a receipt file (stub, can be expanded)
    public File generateReceipt() {
        File file = new File(receiptID + "_receipt.txt");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("===== Booking Receipt =====\n");
            writer.write("Receipt ID: " + receiptID + "\n");
            writer.write("NRIC: " + applicantNRIC + "\n");
            writer.write("Age: " + applicantAge + "\n");
            writer.write("Marital Status: " + maritalStatus + "\n");
            writer.write("Flat Type Booked: " + flatTypeBooked + "\n");
            writer.write("Project: " + project.getProjectName() + "\n");
            writer.write("Booking Date: " + bookingDate.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
