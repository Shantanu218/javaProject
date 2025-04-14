import java.util.List;

public class User {
    private String nric;
    private String password = "default password";
    private int age;
    private String maritalStatus;
    private List<Enquiry> enquiries;

    public User(String nric, String password, int age, String maritalStatus, List<Enquiry> enquiries) {
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.enquiries = enquiries;
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

    // Existing Methods
    public boolean login(String nric, String password) {
        return false;
    }

    public boolean isValidNRIC(String nric) {
        return false;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        return false;
    }

    public String generateEnquiryID() {
        return "";
    }

    public boolean updateAge(int newAge) {
        return false;
    }

    public boolean updateMaritalStatus(String oldStatus, String newStatus) {
        return false;
    }
}
