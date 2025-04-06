import java.util.List;


public class User {
    private String nric;
    private String password = "default password";
    private int age;
    private String maritalStatus;
    private List<Enquiry> enquiries;

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
