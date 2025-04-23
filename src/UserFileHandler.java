// UserFileHandler.java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import enums.MaritalStatus;

/**
 * Handles loading and saving of User data from/to a CSV text file.
 *
 * CSV format (one user per line):
 *    NRIC,password,age,maritalStatus,role
 *
 * role must be "Applicant", "Officer", or "Manager".
 */
public class UserFileHandler {
    private final String filePath;

    public UserFileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads all users from the CSV file and creates the appropriate subclass.
     */
    public List<User> readUserData() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.err.println("Skipping malformed user record: " + line);
                    continue;
                }

                String nric    = parts[0].trim();
                String pwd     = parts[1].trim();
                int age        = Integer.parseInt(parts[2].trim());

                String msStr   = parts[3].trim().toUpperCase();
                MaritalStatus maritalStatus;
                try {
                    maritalStatus = MaritalStatus.valueOf(msStr);
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown marital status: " + parts[3] + "; defaulting to SINGLE");
                    maritalStatus = MaritalStatus.SINGLE;
                }

                String role    = parts[4].trim();
                List<Enquiry> enquiries = new ArrayList<>();
                User user;
                switch (role) {
                    case "Manager":
                        user = new HDBManager(nric, pwd, age, maritalStatus);
                        break;
                    case "Officer":
                        user = new HDBOfficer(nric, pwd, age, maritalStatus);
                        break;
                    case "Applicant":
                    default:
                        user = new Applicant(nric, pwd, age, maritalStatus);
                        break;
                }
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            System.err.println("User data file not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading user data file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid age format in user data: " + e.getMessage());
        }
        return users;
    }

    /**
     * Writes the given list of users back to the CSV file.
     * Only NRIC, password, age, maritalStatus, and role are persisted.
     */
    public void writeUserData(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (User u : users) {
                String role;
                if (u instanceof HDBManager)      role = "Manager";
                else if (u instanceof HDBOfficer) role = "Officer";
                else                               role = "Applicant";

                writer.printf(
                    "%s,%s,%d,%s,%s%n",
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
