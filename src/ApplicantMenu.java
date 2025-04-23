import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.FlatType;
import enums.MaritalStatus;

/**
 * CLI menu for Applicants per the assignment:
 * - View available projects
 * - Apply for one project
 * - View application status
 * - Request withdrawal
 * - Manage enquiries
 */
public class ApplicantMenu extends Menu {
    private final Applicant applicant;
    private final List<Project> projectRepo;
    private final Scanner scanner = getScanner();

    /**
     * @param applicant   the logged-in applicant
     * @param projectRepo list of all projects
     */
    public ApplicantMenu(Applicant applicant, List<Project> projectRepo) {
        this.applicant   = applicant;
        this.projectRepo = projectRepo;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. View Available Projects");
        System.out.println("2. Apply for Project");
        System.out.println("3. View My Application Status");
        System.out.println("4. Request Withdrawal");
        System.out.println("5. Manage Enquiries");
        System.out.println("0. Logout");
        System.out.print("Select an option: ");
    }

    @Override
    public void handleOption(int option) {
        switch (option) {
            case 1: viewAvailableProjects();    break;
            case 2: applyForProject();          break;
            case 3: viewApplicationStatus();    break;
            case 4: requestWithdrawal();        break;
            case 5: manageEnquiries();          break;
            case 0:
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid choice.");
        }
        run();
    }

    private void viewAvailableProjects() {
        List<Project> open = projectRepo.stream()
            .filter(p -> p.isVisible() && p.isWithinApplicationPeriod())
            .filter(p ->
                applicant.isEligible(p, FlatType.Two_Room)
             || applicant.isEligible(p, FlatType.Three_Room)
            )
            .collect(Collectors.toList());

        if (open.isEmpty()) {
            System.out.println("No projects available at the moment.");
        } else {
            System.out.println("\n-- Available Projects --");
            open.forEach(p -> System.out.printf(
                "%s: %s (%s) [2-Room: %d, 3-Room: %d]%n",
                p.getProjectID(),
                p.getProjectName(),
                p.getNeighborhood(),
                p.getTwoRoomUnits(),
                p.getThreeRoomUnits()
            ));
        }
    }

    private void applyForProject() {
        System.out.print("Enter Project ID to apply: ");
        String pid = scanner.nextLine().trim();
        Project chosen = projectRepo.stream()
            .filter(p -> p.getProjectID().equals(pid))
            .findFirst().orElse(null);
        if (chosen == null) {
            System.out.println("Project not found.");
            return;
        }

        FlatType ft;
        if (applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
            ft = FlatType.Two_Room;
        } else {
            ft = (chosen.getThreeRoomUnits() > 0)
                ? FlatType.Three_Room
                : FlatType.Two_Room;
        }

        try {
            applicant.apply(chosen, ft);
            System.out.println("Application submitted and is now Pending.");
        } catch (IllegalStateException e) {
            System.out.println("Cannot apply: " + e.getMessage());
        }
    }

    private void viewApplicationStatus() {
        Application app = applicant.getApplication();
        if (app == null) {
            System.out.println("You have not applied for any project.");
        } else {
            System.out.println(app);
        }
    }

    private void requestWithdrawal() {
        boolean ok = applicant.requestWithdrawal();
        System.out.println(ok
            ? "Withdrawal request submitted."
            : "Unable to withdraw—no application or already requested.");
    }

    private void manageEnquiries() {
        System.out.println("\n-- Manage Enquiries --");
        System.out.println("1. Submit Enquiry");
        System.out.println("2. View My Enquiries");
        System.out.println("3. Edit Enquiry");
        System.out.println("4. Delete Enquiry");
        System.out.println("0. Back");
        System.out.print("Choice: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1:
                System.out.print("Project ID: ");
                String pid = scanner.nextLine().trim();
                System.out.print("Enquiry text: ");
                String text = scanner.nextLine().trim();
                new Enquiry(
                    applicant,
                    projectRepo.stream()
                               .filter(p -> p.getProjectID().equals(pid))
                               .findFirst()
                               .orElseThrow(() -> new IllegalArgumentException("Project not found")),
                    text
                );
                System.out.println("Enquiry submitted.");
                break;

            case 2:
                List<Enquiry> list = applicant.getEnquiries();
                if (list.isEmpty()) {
                    System.out.println("You have no enquiries.");
                } else {
                    System.out.println("\n-- Your Enquiries --");
                    list.forEach(e -> System.out.printf(
                        "%d: [%s] %s (Status: %s)%n",
                        e.getEnquiryID(),
                        e.getProject().getProjectID(),
                        e.getEnquiryText(),
                        e.getStatus()
                    ));
                }
                break;

            case 3:
                System.out.print("Enquiry ID to edit: ");
                int editId = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("New enquiry text: ");
                String newText = scanner.nextLine().trim();
                applicant.getEnquiries().stream()
                    .filter(e -> e.getEnquiryID() == editId)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Enquiry not found"))
                    .edit(newText);
                System.out.println("Enquiry updated.");
                break;

            case 4:
                System.out.print("Enquiry ID to delete: ");
                int delId = Integer.parseInt(scanner.nextLine().trim());
                Enquiry toDelete = applicant.getEnquiries().stream()
                    .filter(e -> e.getEnquiryID() == delId)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Enquiry not found"));
                toDelete.delete();
                System.out.println("Enquiry deleted.");
                break;

            case 0:
                return;

            default:
                System.out.println("Invalid choice.");
        }
    }
}
