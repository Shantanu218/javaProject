import enums.FlatType;
import enums.MaritalStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A plain-text receipt for a successful flat booking.
 */
public class Receipt {
    private final String receiptID;
    private final String applicantName;
    private final String applicantNRIC;
    private final int applicantAge;
    private final MaritalStatus maritalStatus;
    private final FlatType flatTypeBooked;
    private final Project project;
    private final LocalDateTime bookingDate;

    /**
     * @param receiptID        unique ID, e.g. "R-APP-1234-<timestamp>"
     * @param applicantName    full name of the applicant (add getName() to Applicant)
     * @param applicantNRIC    NRIC of the applicant
     * @param applicantAge     age of the applicant
     * @param maritalStatus    SINGLE or MARRIED
     * @param flatTypeBooked   the FlatType they booked
     * @param project          the Project they booked in
     * @param bookingDate      date/time of booking
     */
    public Receipt(
        String receiptID,
        String applicantName,
        String applicantNRIC,
        int applicantAge,
        MaritalStatus maritalStatus,
        FlatType flatTypeBooked,
        Project project,
        LocalDateTime bookingDate
    ) {
        this.receiptID       = receiptID;
        this.applicantName   = applicantName;
        this.applicantNRIC   = applicantNRIC;
        this.applicantAge    = applicantAge;
        this.maritalStatus   = maritalStatus;
        this.flatTypeBooked  = flatTypeBooked;
        this.project         = project;
        this.bookingDate     = bookingDate;
    }

    /**
     * Generates a text file named "<receiptID>_receipt.txt" with the booking details.
     * @return the Path to the generated file.
     * @throws IOException if an I/O error occurs.
     */
    public Path generateReceiptFile() throws IOException {
        String filename = receiptID + "_receipt.txt";
        Path file = Path.of(filename);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String content = new StringBuilder()
            .append("===== Booking Receipt =====\n")
            .append("Receipt ID: ").append(receiptID).append("\n")
            .append("Name: ").append(applicantName).append("\n")
            .append("NRIC: ").append(applicantNRIC).append("\n")
            .append("Age: ").append(applicantAge).append("\n")
            .append("Marital Status: ").append(maritalStatus).append("\n")
            .append("Flat Type Booked: ").append(flatTypeBooked).append("\n")
            .append("Project: ")
              .append(project.getProjectName())
              .append(" (").append(project.getNeighborhood()).append(")\n")
            .append("Booking Date: ")
              .append(bookingDate.format(dtf)).append("\n")
            .toString();

        Files.writeString(file, content);
        return file;
    }
}
