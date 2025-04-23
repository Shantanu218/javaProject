import java.util.List;
import java.util.Scanner;
import enums.FlatType;
import enums.ApplicationStatus;

/**
 * CLI menu for HDB Officer.
 * Boundary layer only: reads input, prints output, delegates to the HDBOfficer domain object.
 */
public class HDBOfficerMenu extends Menu {
    private final HDBOfficer officer;
    private final List<Project> projectRepo;
    private final Scanner scanner = getScanner();

    public HDBOfficerMenu(HDBOfficer officer, List<Project> projectRepo) {
        this.officer     = officer;
        this.projectRepo = projectRepo;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== HDB Officer Menu ===");
        System.out.println("1. View Open Projects");
        System.out.println("2. Register for Project");
        System.out.println("3. View Registration Status");
        System.out.println("4. View Handled Project Details");
        System.out.println("5. View Enquiries on Handled Project");
        System.out.println("6. Reply to Enquiry");
        System.out.println("7. Book Flat for Applicant");
        System.out.println("0. Logout");
        System.out.print("Choice: ");
    }

    @Override
    public void handleOption(int option) {
        try {
            switch (option) {
                case 1: viewOpenProjects();      break;
                case 2: registerForProject();    break;
                case 3: viewRegStatus();         break;
                case 4: viewProjectDetails();    break;
                case 5: viewEnquiries();         break;
                case 6: replyToEnquiry();        break;
                case 7: bookFlat();              break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        run();
    }

    private void viewOpenProjects() {
        System.out.println("\n-- Open Projects --");
        projectRepo.stream()
            .filter(p -> p.isWithinApplicationPeriod() && p.isVisible())
            .forEach(p -> System.out.println(p));
    }

    private void registerForProject() {
        System.out.print("Enter Project ID: ");
        String pid = scanner.nextLine().trim();
        Project p = projectRepo.stream()
            .filter(proj -> proj.getProjectID().equals(pid))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        officer.registerForProject(p);
        System.out.println("Registration requested (Pending).");
    }

    private void viewRegStatus() {
        System.out.println("Registration Status: " + officer.getRegistrationStatus());
    }

    private void viewProjectDetails() {
        Project p = officer.getHandledProject();
        if (p == null) {
            System.out.println("You have not been assigned any project.");
        } else {
            System.out.println("\n-- Handled Project Details --");
            System.out.println(p);
        }
    }

    private void viewEnquiries() {
        Project p = officer.getHandledProject();
        if (p == null) {
            System.out.println("No project assigned.");
            return;
        }
        List<Enquiry> enquiries = p.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries on this project.");
        } else {
            System.out.println("\n-- Enquiries --");
            enquiries.forEach(e -> System.out.printf(
                "%d: %s (Status: %s)%n", 
                e.getEnquiryID(), e.getEnquiryText(), e.getStatus()
            ));
        }
    }

    private void replyToEnquiry() {
        System.out.print("Enter Enquiry ID to reply: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Reply text: ");
        String reply = scanner.nextLine().trim();
        Enquiry e = officer.getHandledProject().getEnquiries().stream()
            .filter(en -> en.getEnquiryID() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Enquiry not found"));
        officer.replyToEnquiry(e, reply);
        System.out.println("Enquiry replied.");
    }

    private void bookFlat() {
        System.out.print("Enter Application ID to book: ");
        String aid = scanner.nextLine().trim();
        // find the application in the handled project
        Application app = officer.getHandledProject().getApplications().stream()
            .filter(a -> a.getApplicationID().equals(aid))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        // ensure it’s successful
        if (app.getStatus() != ApplicationStatus.SUCCESSFUL) {
            throw new IllegalStateException("Only successful applications can be booked");
        }
        officer.bookFlat(app);
        System.out.println("Application booked. Status is now BOOKED.");
    }
}
