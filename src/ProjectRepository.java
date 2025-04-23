// ProjectRepository.java

import java.util.List;

/**
 * Repository abstraction for Projects.
 */
public interface ProjectRepository {
    /** Return all projects loaded in memory. */
    List<Project> findAll();

    /** Look up a project by its ID, or null if not found. */
    Project findById(String projectID);

    /** Persist the given list of projects. */
    void saveAll(List<Project> projects);
}
