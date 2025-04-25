// CsvProjectRepository.java

import java.util.ArrayList;
import java.util.List;

/**
 * A CSV‐backed implementation of ProjectRepository.
 * For now, this stub just keeps projects in memory; hook up your file I/O as needed.
 */
public class CsvProjectRepository implements ProjectRepository {
    private final String filePath;
    private List<Project> cache;

    public CsvProjectRepository(String filePath) {
        this.filePath = filePath;
        // TODO: load from CSV file into `cache`
        this.cache = new ArrayList<>();
    }

    @Override
    public List<Project> findAll() {
        return cache;
    }

    @Override
    public Project findById(String projectID) {
        for (Project p : cache) {
            if (p.getProjectID().equals(projectID)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void saveAll(List<Project> projects) {
        this.cache = projects;
        // TODO: write `projects` back to CSV at filePath
    }
}
