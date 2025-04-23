// UserRepository.java

import java.util.List;

/**
 * Repository abstraction for loading/saving Users.
 */
public interface UserRepository {
    /** Find a user by NRIC, or return null if not found. */
    User findByNric(String nric);

    /** Return all users currently loaded. */
    List<User> findAll();

    /** Persist the given list of users back to storage. */
    void saveAll(List<User> users);
}

