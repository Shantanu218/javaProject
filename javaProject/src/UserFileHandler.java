import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import enums.MaritalStatus;

/**
 * Handles loading and saving of User data from/to a CSV text file.
 *
 * Expected CSV columns (with a header row that will be skipped):
 *    nric,password,age,maritalStatus,role
 */
public class UserFileHandler {
    private final String filePath;

    public UserFileHandler(String filePath) {
        this.filePath = filePath;
    }
    /**
     * Reads all users from the CSV file and creates the appropriate subclass.
     * Skips blank lines, comments (starting with '#'), and the header row.
     */
    public List<User> readUserData() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                // Skip blank lines, comments, or header row
                if (trimmed.isEmpty()
                        || trimmed.startsWith("#")
                        || trimmed.toLowerCase().startsWith("nric,")) {
                    continue;
                }
                String[] parts = trimmed.split(",");
                if (parts.length < 5) {
                    System.err.println("Skipping malformed user record: " + trimmed);
                    continue;
                }

                String nric = parts[0].trim();
                String pwd  = parts[1].trim();
                int age;
                try {
                    age = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid age in user record: " + parts[2] + " (NRIC: " + nric + ")");
                    continue;
                }
                MaritalStatus ms;
                try {
                    ms = MaritalStatus.valueOf(parts[3].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown marital status: " + parts[3]
                            + " for user " + nric + "; defaulting to SINGLE");
                    ms = MaritalStatus.SINGLE;
                }

                String role = parts[4].trim();
                User user;
                switch (role) {
                    case "Manager":
                        user = new HDBManager(nric, pwd, age, ms);
                        break;
                    case "Officer":
                        user = new HDBOfficer(nric, pwd, age, ms);
                        break;
                    default:  // Applicant
                        user = new Applicant(nric, pwd, age, ms);
                        break;
                }
                // System.out.println("Parsed user: " + nric + ", " + pwd + ", " + age + ", " + ms + ", " + role);
                users.add(user);
//                System.out.println("Added user: " + user);
            }
        } catch (IOException e) {
            System.err.println("Error reading user data file: " + e.getMessage());
        }
        System.out.println("Finished reading users. Count: " + users.size());
        return users;
    }

    /**
     * Writes the given list of users back to the CSV file.
     * Each line is: nric,password,age,maritalStatus,role
     */
    public void writeUserData(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (User u : users) {
                String role;
                if (u instanceof HDBManager)       role = "Manager";
                else if (u instanceof HDBOfficer)  role = "Officer";
                else                                role = "Applicant";

                writer.printf("%s,%s,%d,%s,%s%n",
                    u.getNric(),
                    u.getPassword(),
                    u.getAge(),
                    u.getMaritalStatus().name(),
                    role
                );
            }
        } catch (IOException e) {
            System.err.println("Error writing user data file: " + e.getMessage());
        }
    }
}
