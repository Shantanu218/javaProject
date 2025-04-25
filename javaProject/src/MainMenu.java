// MainMenu.java

import java.util.List;
import java.util.Scanner;

/**
 * Top-level CLI menu.  Delegates to role‐specific submenus.
 */
public class MainMenu extends Menu {
    private final LoginHandler auth;
    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final OfficerRegistrationRepository regRepo;

    public MainMenu(LoginHandler auth,
                    UserRepository userRepo,
                    ProjectRepository projectRepo,
                    OfficerRegistrationRepository regRepo) {
        this.auth        = auth;
        this.userRepo    = userRepo;
        this.projectRepo = projectRepo;
        this.regRepo     = regRepo;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Welcome to BTO Management System ===");
        System.out.println("1. Log in");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    @Override
    public void handleOption(int option) {
        switch (option) {
            case 1:
                loginFlow();
                break;
            case 0:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void loginFlow() {
        Scanner sc = getScanner();
        System.out.print("NRIC: ");
        String nric = sc.nextLine().trim();
        System.out.print("Password: ");
        String pwd  = sc.nextLine().trim();

        try {
            User user = auth.authenticate(nric, pwd);

            if (user instanceof HDBManager) {
                new HDBManagerMenu(
                    (HDBManager) user,
                    projectRepo.findAll(),
                    regRepo.findAll()
                ).run();

            } else if (user instanceof HDBOfficer) {
                new HDBOfficerMenu(
                    (HDBOfficer) user,
                    projectRepo.findAll()
                ).run();

            } else if (user instanceof Applicant) {
                new ApplicantMenu(
                    (Applicant) user,
                    projectRepo.findAll()
                ).run();

            } else {
                System.out.println("Unknown role.");
            }

        } catch (IllegalArgumentException | AuthenticationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
