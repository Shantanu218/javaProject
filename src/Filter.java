import enums.FlatType;
import enums.MaritalStatus;

/**
 * Holds criteria for filtering Applicants and Projects (or Applications).
 */
public class Filter {
    private MaritalStatus maritalStatus;  // e.g. SINGLE or MARRIED
    private FlatType flatType;            // e.g. Two_Room or Three_Room
    private Integer minAge;               // inclusive
    private Integer maxAge;               // inclusive
    private String projectName;           // exact match

    // — Setters —

    public void setMaritalStatus(String ms) {
        if (ms != null && !ms.isBlank()) {
            this.maritalStatus = MaritalStatus.valueOf(ms.trim().toUpperCase());
        }
    }

    public void setFlatType(String ft) {
        if (ft != null && !ft.isBlank()) {
            // e.g. "2-Room" → "Two_Room"
            String enumName = ft.trim()
                                .replace("-", "_")
                                .replace(" ", "_");
            this.flatType = FlatType.valueOf(enumName);
        }
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setProjectName(String projectName) {
        if (projectName != null && !projectName.isBlank()) {
            this.projectName = projectName.trim();
        }
    }

    // — Matching logic —

    /**
     * Returns true if the given project satisfies the flat‐type and project‐name criteria.
     */
    public boolean matches(Project project) {
        if (projectName != null && 
            !project.getProjectName().equalsIgnoreCase(projectName)) {
            return false;
        }
        if (flatType != null) {
            // require that project has at least one unit of that type
            if (flatType == FlatType.Two_Room && project.getTwoRoomUnits() == 0) {
                return false;
            }
            if (flatType == FlatType.Three_Room && project.getThreeRoomUnits() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given applicant & project satisfy all non‐null criteria.
     */
    public boolean matches(Applicant applicant, Project project) {
        // marital status
        if (maritalStatus != null 
         && applicant.getMaritalStatus() != maritalStatus) {
            return false;
        }
        // age range
        if (minAge != null && applicant.getAge() < minAge) return false;
        if (maxAge != null && applicant.getAge() > maxAge) return false;
        // flat‐type availability
        if (flatType != null) {
            if (flatType == FlatType.Two_Room && project.getTwoRoomUnits() == 0) {
                return false;
            }
            if (flatType == FlatType.Three_Room && project.getThreeRoomUnits() == 0) {
                return false;
            }
        }
        // project name
        if (projectName != null 
         && !project.getProjectName().equalsIgnoreCase(projectName)) {
            return false;
        }
        return true;
    }
}
