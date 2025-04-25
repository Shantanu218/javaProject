// Main.java

public class Main {
    public static void main(String[] args) {
        // 1) Initialize repositories
        UserRepository userRepo          = new CsvUserRepository("C:\\Users\\cring\\Desktop\\sc2002Project\\javaProject\\src\\users.csv");
        System.out.println(userRepo.findAll());
        ProjectRepository projectRepo    = new CsvProjectRepository("C:\\Users\\cring\\Desktop\\sc2002Project\\javaProject\\src\\projects.csv");
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
