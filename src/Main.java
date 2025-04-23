// Main.java

public class Main {
    public static void main(String[] args) {
        // 1) Initialize repositories
        UserRepository userRepo          = new CsvUserRepository("users.csv");
        ProjectRepository projectRepo    = new CsvProjectRepository("projects.csv");
        OfficerRegistrationRepository regRepo =
            new InMemoryOfficerRegistrationRepository();

        // 2) Authentication handler
        LoginHandler auth = new LoginHandler(userRepo);

        // 3) Launch CLI
        MainMenu menu = new MainMenu(auth, userRepo, projectRepo, regRepo);
        menu.run();

        // 4) On exit, persist any changes
        userRepo.saveAll(userRepo.findAll());
        projectRepo.saveAll(projectRepo.findAll());
        regRepo.saveAll(regRepo.findAll());
    }
}
