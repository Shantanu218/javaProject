import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import enums.RegistrationStatus;
import enums.ApplicationStatus;
import enums.FlatType;

/**
 * CLI menu for HDB Manager role.
 * Boundary layer only: reads input, prints output, delegates to the HDBManager domain object.
 */
public class HDBManagerMenu extends Menu {
    private final HDBManager manager;
    private final List<Project> projectRepo;
    private final List<OfficerRegistration> officerRegistrations;
    private final Scanner scanner = getScanner();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param manager               the logged-in HDBManager
     * @param projectRepo           all projects in the system
     * @param officerRegistrations  all officer registrations in the system
     */
    public HDBManagerMenu(HDBManager manager,
                          List<Project> projectRepo,
                          List<OfficerRegistration> officerRegistrations) {
        this.manager               = manager;
        this.projectRepo           = projectRepo;
        this.officerRegistrations  = officerRegistrations;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== HDB Manager Menu ===");
        System.out.println("1.  Create Project");
        System.out.println("2.  Edit Project");
        System.out.println("3.  Delete Project");
        System.out.println("4.  Toggle Project Visibility");
        System.out.println("5.  View All Projects");
        System.out.println("6.  View My Projects");
        System.out.println("7.  View Officer Registrations");
        System.out.println("8.  Approve Officer Registration");
        System.out.println("9.  Reject Officer Registration");
        System.out.println("10. Approve Application");
        System.out.println("11. Reject Application");
        System.out.println("12. Process Withdrawal Request");
        System.out.println("13. Generate Flat Booking Report");
        System.out.println("0.  Logout");
        System.out.print("Select an option: ");
    }

    @Override
    public void handleOption(int option) {
        try {
            switch (option) {
                case 1:  createProject();             break;
                case 2:  editProject();               break;
                case 3:  deleteProject();             break;
                case 4:  toggleVisibility();          break;
                case 5:  viewAllProjects();           break;
                case 6:  viewMyProjects();            break;
                case 7:  viewOfficerRegistrations();  break;
                case 8:  approveOfficerRegistration();break;
                case 9:  rejectOfficerRegistration(); break;
                case 10: approveApplication();        break;
                case 11: rejectApplication();         break;
                case 12: processWithdrawal();         break;
                case 13: generateReport();            break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        run();
    }

    private void createProject() {
        System.out.print("Project ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Neighborhood: ");
        String nb = scanner.nextLine().trim();
        System.out.print("2-Room Units: ");
        int two = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("3-Room Units: ");
        int three = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Opening Date (YYYY-MM-DD): ");
        LocalDate open = LocalDate.parse(scanner.nextLine().trim(), dtf);
        System.out.print("Closing Date (YYYY-MM-DD): ");
        LocalDate close = LocalDate.parse(scanner.nextLine().trim(), dtf);
        System.out.print("Officer Slots: ");
        int slots = Integer.parseInt(scanner.nextLine().trim());

        Project p = new Project(id, name, nb, two, three, open, close, manager, slots);
        manager.createProject(p);
        projectRepo.add(p);
        System.out.println("Project created: " + p.getProjectID());
    }

    private void editProject() {
        System.out.print("Enter Project ID to edit: ");
        String id = scanner.nextLine().trim();
        Project existing = projectRepo.stream()
            .filter(p -> p.getProjectID().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        System.out.print("New Name ["+existing.getProjectName()+"]: ");
        String inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) existing.setProjectName(inp);
        System.out.print("New Neighborhood ["+existing.getNeighborhood()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) existing.setNeighborhood(inp);
        System.out.print("2-Room Units ["+existing.getTwoRoomUnits()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) existing.setTwoRoomUnits(Integer.parseInt(inp));
        System.out.print("3-Room Units ["+existing.getThreeRoomUnits()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) existing.setThreeRoomUnits(Integer.parseInt(inp));
        System.out.print("Opening Date ["+existing.getApplicationOpeningDate()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) existing.setApplicationOpeningDate(LocalDate.parse(inp, dtf));
        System.out.print("Closing Date ["+existing.getApplicationClosingDate()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) existing.setApplicationClosingDate(LocalDate.parse(inp, dtf));

        manager.updateProject(id, existing);
        System.out.println("Project updated.");
    }

    private void deleteProject() {
        System.out.print("Enter Project ID to delete: ");
        String id = scanner.nextLine().trim();
        if (manager.deleteProject(id)) {
            projectRepo.removeIf(p -> p.getProjectID().equals(id));
            System.out.println("Project deleted.");
        } else {
            System.out.println("Delete failed or unauthorized.");
        }
    }

    private void toggleVisibility() {
        System.out.print("Project ID: ");
        String id = scanner.nextLine().trim();
        manager.toggleProjectVisibility(id);
        Project p = projectRepo.stream()
            .filter(proj -> proj.getProjectID().equals(id))
            .findFirst().get();
        System.out.println("Visibility now: " + p.isVisible());
    }

    private void viewAllProjects() {
        System.out.println("\n-- All Projects --");
        projectRepo.forEach(p -> System.out.println(p));
    }

    private void viewMyProjects() {
        System.out.println("\n-- My Projects --");
        projectRepo.stream()
            .filter(p -> p.getManagerInCharge().equals(manager))
            .forEach(p -> System.out.println(p));
    }

    private void viewOfficerRegistrations() {
        System.out.print("Project ID: ");
        String id = scanner.nextLine().trim();
        System.out.println("\n-- Officer Registrations --");
        officerRegistrations.stream()
            .filter(r -> r.getProject().getProjectID().equals(id))
            .forEach(r -> System.out.println(r));
    }

    private void approveOfficerRegistration() {
        System.out.print("Registration ID to approve: ");
        int rid = Integer.parseInt(scanner.nextLine().trim());
        manager.approveOfficerRegistration(rid);
        System.out.println("Registration approved.");
    }

    private void rejectOfficerRegistration() {
        System.out.print("Registration ID to reject: ");
        int rid = Integer.parseInt(scanner.nextLine().trim());
        manager.rejectOfficerRegistration(rid);
        System.out.println("Registration rejected.");
    }

    private void approveApplication() {
        System.out.print("Application ID to approve: ");
        String aid = scanner.nextLine().trim();
        manager.approveApplication(aid);
        System.out.println("Application approved.");
    }

    private void rejectApplication() {
        System.out.print("Application ID to reject: ");
        String aid = scanner.nextLine().trim();
        manager.rejectApplication(aid);
        System.out.println("Application rejected.");
    }

    private void processWithdrawal() {
        System.out.print("Application ID to withdraw: ");
        String aid = scanner.nextLine().trim();
        manager.processWithdrawal(aid);
        System.out.println("Withdrawal processed.");
    }

    private void generateReport() {
        String report = manager.generateFlatBookingReport();
        System.out.println("\n-- Flat Booking Report --");
        System.out.println(report.isEmpty() ? "No bookings." : report);
    }
}
