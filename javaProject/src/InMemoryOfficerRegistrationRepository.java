// InMemoryOfficerRegistrationRepository.java

import java.util.ArrayList;
import java.util.List;

/**
 * Simple in‐memory implementation of OfficerRegistrationRepository.
 */
public class InMemoryOfficerRegistrationRepository implements OfficerRegistrationRepository {
    private List<OfficerRegistration> cache = new ArrayList<>();

    @Override
    public List<OfficerRegistration> findAll() {
        return cache;
    }

    @Override
    public OfficerRegistration findById(int registrationID) {
        for (OfficerRegistration r : cache) {
            if (r.getId() == registrationID) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void saveAll(List<OfficerRegistration> registrations) {
        this.cache = registrations;
        // No file‐I/O; just keep in memory
    }
}
