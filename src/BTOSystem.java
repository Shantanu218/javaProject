import java.util.ArrayList;
import java.util.List;


public class BTOSystem {
    private List<User> users;
    private List<Project> projects;
    private User currentUser;
    private UserFileHandler userFileHandler;

    public void start() {
    }

    public User login(String nric, String password) {
        return null;
    }

    public void loadUsers(String filePath) {
    }

    public void loadProjects(String filePath) {
    }

    public List<User> getUsers() {
        return new ArrayList<>();
    }

    public List<Project> getProjects() {
        return new ArrayList<>();
    }

    public User getCurrentUser() {
        return null;
    }

    public void setCurrentUser(User user) {
    }

    public void dispatchMenu() {
    }

    public void exit() {
    }
}
