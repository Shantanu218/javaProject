import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system.
 * This class can be made abstract if you do not plan to instantiate it directly.
 */
public abstract class User {
    private String nric;
    private String password = "password";  // Default password as per assignment
    private int age;
    private String maritalStatus;
    private List<Enquiry> enquiries;

    /**
     * Constructs a User with the given details.
     *
     * @param nric          the NRIC of the user
     * @param password      the user's password
     * @param age           the age of the user
     * @param maritalStatus the marital status of the user
     * @param enquiries     the list of enquiries associated with the user
     */
    public User(String nric, String password, int age, String maritalStatus, List<Enquiry> enquiries) {
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        // Initialize to an empty list if null is provided.
        this.enquiries = (enquiries != null) ? enquiries : new ArrayList<>();
    }

    // Getters and Setters
    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void setEnquiries(List<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }

    // Business Methods

    /**
     * Validates and logs in a user with given credentials.
     *
     * @param nric     the NRIC provided for login
     * @param password the password provided for login
     * @return true if login is successful, false otherwise
     */
    public boolean login(String nric, String password) {
        return this.nric.equals(nric) && this.password.equals(password);
    }

    /**
     * Validates if the provided NRIC matches the expected format.
     *
     * @param nric the NRIC to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidNRIC(String nric) {
        // A simple regex check can be implemented here.
        // For instance: NRIC should start with S/T, followed by 7 digits, ending with a letter.
        return nric.matches("^[ST]\\d{7}[A-Z]$");
    }

    /**
     * Changes the user’s password if the old password matches.
     *
     * @param oldPassword the current password
     * @param newPassword the new password to set
     * @return true if password changed successfully; false otherwise
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    /**
     * Generates a unique enquiry ID.
     * This is a placeholder implementation.
     *
     * @return a unique enquiry ID string
     */
    public String generateEnquiryID() {
        // Implementation can be enhanced with a counter or UUID.
        return "ENQ-" + System.currentTimeMillis();
    }

    /**
     * Updates the user's age.
     *
     * @param newAge the new age to set
     * @return true if update successful; false otherwise (e.g., invalid age)
     */
    public boolean updateAge(int newAge) {
        if (newAge > 0) {
            this.age = newAge;
            return true;
        }
        return false;
    }

    /**
     * Updates the user's marital status if the current status matches the provided old status.
     *
     * @param oldStatus the current marital status to check against
     * @param newStatus the new marital status to set
     * @return true if update successful; false otherwise
     */
    public boolean updateMaritalStatus(String oldStatus, String newStatus) {
        if (this.maritalStatus.equals(oldStatus)) {
            this.maritalStatus = newStatus;
            return true;
        }
        return false;
    }
}