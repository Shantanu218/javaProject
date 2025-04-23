// LoginHandler.java

import java.util.regex.Pattern;

/**
 * Handles authentication of users by NRIC/password.
 * Control‐layer class (no direct I/O).
 */
public class LoginHandler {
    private static final Pattern NRIC_PATTERN =
        Pattern.compile("^[ST]\\d{7}[A-Z]$");

    private final UserRepository userRepo;

    public LoginHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Authenticate the given credentials.
     * @throws IllegalArgumentException if NRIC format is invalid
     * @throws AuthenticationException  if no such user or wrong password
     */
    public User authenticate(String nric, String password) {
        if (!isValidUserID(nric)) {
            throw new IllegalArgumentException("Invalid NRIC format");
        }
        User user = userRepo.findByNric(nric);
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Login failed: wrong credentials");
        }
        return user;
    }

    /** Checks NRIC format: S/T + 7 digits + uppercase letter. */
    public boolean isValidUserID(String nric) {
        return nric != null && NRIC_PATTERN.matcher(nric).matches();
    }
}
