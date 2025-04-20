import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * CLI menu for HDB Manager role, exposing all capabilities per the assignment handout.
 */
public class HDBManagerMenu extends Menu {
    private final HDBManager manager;
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public HDBManagerMenu(HDBManager manager) {
        this.manager = manager;
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
        System.out.println("12. Approve Withdrawal Request");
        System.out.println("13. Reject Withdrawal Request");
        System.out.println("14. Generate Flat Booking Report");
        System.out.println("15. View All Enquiries");
        System.out.println("16. Reply to Enquiry");
        System.out.println("0.  Logout");
        System.out.print("Select an option: ");
    }

    @Override
    public void handleOption(int option) {
        switch (option) {
            case 1:  createProject(); break;
            case 2:  editProject(); break;
            case 3:  deleteProject(); break;
            case 4:  toggleVisibility(); break;
            case 5:  viewAllProjects(); break;
            case 6:  viewMyProjects(); break;
            case 7:  viewOfficerRegistrations(); break;
            case 8:  approveOfficerRegistration(); break;
            case 9:  rejectOfficerRegistration(); break;
            case 10: approveApplication(); break;
            case 11: rejectApplication(); break;
            case 12: approveWithdrawal(); break;
            case 13: rejectWithdrawal(); break;
            case 14: generateReport(); break;
            case 15: viewAllEnquiries(); break;
            case 16: replyToEnquiry(); break;
            case 0:  System.out.println("Logging out..."); break;
            default: System.out.println("Invalid choice."); 
        }
    }

    private void createProject() {
        System.out.print("Project ID: "); String id = scanner.nextLine().trim();
        System.out.print("Name: "); String name = scanner.nextLine().trim();
        System.out.print("Neighborhood: "); String nb = scanner.nextLine().trim();
        System.out.print("Flat Type (2-Room/3-Room): "); String ft = scanner.nextLine().trim();
        System.out.print("2-Room Units: "); int two = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("3-Room Units: "); int three = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Opening Date (YYYY-MM-DD): "); LocalDate open = LocalDate.parse(scanner.nextLine().trim(), dtf);
        System.out.print("Closing Date (YYYY-MM-DD): "); LocalDate close = LocalDate.parse(scanner.nextLine().trim(), dtf);
        System.out.print("Officer Slots: "); int slots = Integer.parseInt(scanner.nextLine().trim());

        Project p = new Project(id, name, nb, ft, two, three, open, close, manager, slots);
        manager.createProject(p);
        System.out.println("Project created: " + p.getProjectName());
    }

    private void editProject() {
        System.out.print("Enter Project ID to edit: ");
        String id = scanner.nextLine().trim();
        Project p = findProjectById(id, manager.viewCreatedProjects());
        if (p == null) { System.out.println("Not found."); return; }

        System.out.print("New Name ["+p.getProjectName()+"]: ");
        String inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) p.setProjectName(inp);

        System.out.print("New Neighborhood ["+p.getNeighborhood()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) p.setNeighborhood(inp);

        System.out.print("2-Room Units ["+p.getTwoRoomUnits()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) p.setTwoRoomUnits(Integer.parseInt(inp));

        System.out.print("3-Room Units ["+p.getThreeRoomUnits()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) p.setThreeRoomUnits(Integer.parseInt(inp));

        System.out.print("Opening Date ["+p.getApplicationOpeningDate()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) p.setApplicationOpeningDate(LocalDate.parse(inp, dtf));

        System.out.print("Closing Date ["+p.getApplicationClosingDate()+"]: ");
        inp = scanner.nextLine().trim();
        if (!inp.isEmpty()) p.setApplicationClosingDate(LocalDate.parse(inp, dtf));

        System.out.println("Project updated.");
    }

    private void deleteProject() {
        System.out.print("Enter Project ID to delete: ");
        String id = scanner.nextLine().trim();
        Project p = findProjectById(id, manager.viewCreatedProjects());
        if (p != null && manager.deleteProject(p)) {
            System.out.println("Deleted.");
        } else {
            System.out.println("Delete failed.");
        }
    }

    private void toggleVisibility() {
        System.out.print("Project ID: ");
        Project p = findProjectById(scanner.nextLine().trim(), manager.viewCreatedProjects());
        if (p != null) {
            manager.toggleProjectVisibility(p, !p.isVisible());
            System.out.println("Visibility now: " + p.isVisible());
        } else {
            System.out.println("Toggle failed.");
        }
    }

    private void viewAllProjects() {
        System.out.println("\n-- All Projects --");
        for (Project p : manager.viewAllProjects()) {
            System.out.printf("%s: %s [%s], Visible: %b%n",
                p.getProjectID(), p.getProjectName(), p.getNeighborhood(), p.isVisible());
        }
    }

    private void viewMyProjects() {
        System.out.println("\n-- My Projects --");
        for (Project p : manager.viewCreatedProjects()) {
            System.out.printf("%s: %s [%s], Visible: %b%n",
                p.getProjectID(), p.getProjectName(), p.getNeighborhood(), p.isVisible());
        }
    }

    private void viewOfficerRegistrations() {
        System.out.print("Project ID: ");
        Project p = findProjectById(scanner.nextLine().trim(), manager.viewCreatedProjects());
        if (p == null) { System.out.println("None."); return; }
        System.out.println("-- Registrations --");
        for (OfficerRegistration r : manager.viewOfficerRegistrations(p)) {
            System.out.printf("%s → %s%n", r.getOfficer().getNric(), r.getStatus());
        }
    }

    private void approveOfficerRegistration() {
        System.out.print("Project ID: ");
        Project p = findProjectById(scanner.nextLine().trim(), manager.viewCreatedProjects());
        System.out.print("Officer NRIC to approve: ");
        String on = scanner.nextLine().trim();
        for (OfficerRegistration r : manager.viewOfficerRegistrations(p)) {
            if (r.getOfficer().getNric().equals(on)) {
                manager.approveOfficerRegistration(r);
                System.out.println("Approved."); return;
            }
        }
        System.out.println("Not found.");
    }

    private void rejectOfficerRegistration() {
        System.out.print("Project ID: ");
        Project p = findProjectById(scanner.nextLine().trim(), manager.viewCreatedProjects());
        System.out.print("Officer NRIC to reject: ");
        String on = scanner.nextLine().trim();
        for (OfficerRegistration r : manager.viewOfficerRegistrations(p)) {
            if (r.getOfficer().getNric().equals(on)) {
                manager.rejectOfficerRegistration(r);
                System.out.println("Rejected."); return;
            }
        }
        System.out.println("Not found.");
    }

    private void approveApplication() {
        System.out.print("Application ID to approve: ");
        String aid = scanner.nextLine().trim();
        Application app = findApplicationById(aid);
        if (manager.approveApplication(app)) {
            System.out.println("Application approved.");
        } else {
            System.out.println("Approve failed.");
        }
    }

    private void rejectApplication() {
        System.out.print("Application ID to reject: ");
        String aid = scanner.nextLine().trim();
        Application app = findApplicationById(aid);
        if (manager.rejectApplication(app)) {
            System.out.println("Application rejected.");
        } else {
            System.out.println("Reject failed.");
        }
    }

    private void approveWithdrawal() {
        System.out.print("Application ID for withdrawal approval: ");
        String aid = scanner.nextLine().trim();
        Application app = findApplicationById(aid);
        if (manager.approveWithdrawal(app)) {
            System.out.println("Withdrawal approved.");
        } else {
            System.out.println("Approve failed.");
        }
    }

    private void rejectWithdrawal() {
        System.out.print("Application ID for withdrawal rejection: ");
        String aid = scanner.nextLine().trim();
        Application app = findApplicationById(aid);
        if (manager.rejectWithdrawal(app)) {
            System.out.println("Withdrawal rejected.");
        } else {
            System.out.println("Reject failed.");
        }
    }

    private void generateReport() {
        List<Application> allApps = manager.viewAllProjects().stream()
            .flatMap(p -> p.getApplications().stream())
            .toList();
        String rpt = manager.generateFlatBookingReport(allApps);
        System.out.println("\n-- Flat Booking Report --");
        System.out.println(rpt.isEmpty() ? "No bookings." : rpt);
    }

    private void viewAllEnquiries() {
        System.out.println("\n-- All Enquiries --");
        for (Enquiry e : manager.viewAllEnquiries()) {
            System.out.printf("%d | Applicant: %s | \"%s\" [Status: %s]%n",
                e.getEnquiryID(),
                e.getApplicant().getNric(),
                e.getEnquiryText(),
                e.getStatus());
        }
    }

    private void replyToEnquiry() {
        System.out.print("Enquiry ID to reply: ");
        String eid = scanner.nextLine().trim();
        Enquiry e = findEnquiryById(eid);
        System.out.print("Reply text: ");
        String reply = scanner.nextLine().trim();
        if (manager.replyToEnquiry(e, reply)) {
            System.out.println("Replied.");
        } else {
            System.out.println("Reply failed.");
        }
    }

    // Helpers—implement these lookup methods to integrate with your data stores:
    private Project findProjectById(String id, List<Project> list) { /* … */ return null; }
    private Application findApplicationById(String id)         { /* … */ return null; }
    private Enquiry findEnquiryById(String id)                 { /* … */ return null; }
}
