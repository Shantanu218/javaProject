// CsvUserRepository.java

import java.util.List;

/**
 * CSV‐based implementation, delegating to your existing UserFileHandler.
 */
public class CsvUserRepository implements UserRepository {
    private final UserFileHandler fileHandler;
    private List<User> cache;

    public CsvUserRepository(String filePath) {
        this.fileHandler = new UserFileHandler(filePath);
        this.cache       = fileHandler.readUserData();
    }

    @Override
    public User findByNric(String nric) {
        for (User u : cache) {
            if (u.getNric().equals(nric)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return cache;
    }

    @Override
    public void saveAll(List<User> users) {
        this.cache = users;
        fileHandler.writeUserData(users);
    }
}

