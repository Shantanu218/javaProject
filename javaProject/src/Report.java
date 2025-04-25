import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import enums.ApplicationStatus;

/**
 * Generates a booking report of all successful flat bookings,
 * with optional filtering by applicant/project criteria.
 */
public class Report {
    private Filter filter;
    private String reportContent;

    /** Set the filter criteria to apply (or leave null for no extra filtering). */
    public void setFilters(Filter filter) {
        this.filter = filter;
    }

    /**
     * Builds the report over the given applications, writes it to a CSV file,
     * and returns the File handle.
     *
     * @param applications  all applications in the system
     * @return              a CSV file of the booked applications
     * @throws IOException  if writing the file fails
     */
    public File generateReport(List<Application> applications) throws IOException {
        // 1) Filter to only BOOKED applications
        List<Application> booked = applications.stream()
            .filter(app -> app.getStatus() == ApplicationStatus.BOOKED)
            .filter(app -> filter == null
                || filter.matches(app.getApplicant(), app.getProject()))
            .collect(Collectors.toList());

        // 2) Build CSV text
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        sb.append("ApplicantNRIC,Age,MaritalStatus,FlatType,ProjectID,ProjectName,BookingDate\n");
        for (Application app : booked) {
            sb.append(String.format(
                "%s,%d,%s,%s,%s,%s,%s\n",
                app.getApplicant().getNric(),
                app.getApplicant().getAge(),
                app.getApplicant().getMaritalStatus().name(),
                app.getFlatTypeChosen().name(),
                app.getProject().getProjectID(),
                app.getProject().getProjectName(),
                app.getApplicationDate().format(df)
            ));
        }
        reportContent = sb.toString();

        // 3) Write to file
        File file = new File("booking_report.csv");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(reportContent);
        }
        return file;
    }

    /**
     * Returns the last-generated report as a CSV string,
     * or an empty string if none has been generated yet.
     */
    public String getFormattedReport() {
        return reportContent != null ? reportContent : "";
    }
}
