// OfficerRegistrationRepository.java

import java.util.List;

/**
 * Repository abstraction for managing OfficerRegistration entities.
 */
public interface OfficerRegistrationRepository {
    /** Return all registrations currently in memory. */
    List<OfficerRegistration> findAll();

    /** Look up a registration by its numeric ID, or null if not found. */
    OfficerRegistration findById(int registrationID);

    /** Persist the given list of registrations (e.g. back to storage). */
    void saveAll(List<OfficerRegistration> registrations);
}
