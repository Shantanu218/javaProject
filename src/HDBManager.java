import enums.FlatType;
import enums.ApplicationStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * HDBManager is responsible for managing BTO project listings,
 * processing officer registrations, handling applications and withdrawals,
 * generating booking reports, and replying to enquiries.
 */
public class HDBManager extends User {
    private static final List<Project> allProjects = new ArrayList<>();
    private static final List<OfficerRegistration> allOfficerRegistrations = new ArrayList<>();

    public HDBManager(String nric, String password, int age, String maritalStatus, List<Enquiry> enquiries) {
        super(nric, password, age, maritalStatus, enquiries);
    }

    /* =====================
       PROJECT MANAGEMENT
       ===================== */

    public List<Project> createProject(Project project) {
        project.setCreatedBy(this);
        allProjects.add(project);
        return allProjects;
    }

    public void updateProject(Project oldProject, Project updatedProject) {
        if (!allProjects.contains(oldProject)) {
            System.out.println("Update failed: Project not found.");
            return;
        }
        if (oldProject.getManagerInCharge() != this) {
            System.out.println("Update failed: Not your project.");
            return;
        }
        oldProject.setProjectName(updatedProject.getProjectName());
        oldProject.setNeighborhood(updatedProject.getNeighborhood());
        oldProject.setTwoRoomUnits(updatedProject.getTwoRoomUnits());
        oldProject.setThreeRoomUnits(updatedProject.getThreeRoomUnits());
        oldProject.setApplicationOpeningDate(updatedProject.getApplicationOpeningDate());
        oldProject.setApplicationClosingDate(updatedProject.getApplicationClosingDate());
    }

    public boolean deleteProject(Project project) {
        if (!allProjects.contains(project)) {
            System.out.println("Delete failed: Project not found.");
            return false;
        }
        if (project.getManagerInCharge() != this) {
            System.out.println("Delete failed: Not your project.");
            return false;
        }
        allProjects.remove(project);
        return true;
    }

    public boolean toggleProjectVisibility(Project project, boolean visibility) {
        if (!allProjects.contains(project)) {
            System.out.println("Toggle failed: Project not found.");
            return false;
        }
        if (project.getManagerInCharge() != this) {
            System.out.println("Toggle failed: Not your project.");
            return false;
        }
        project.setVisibility(visibility);
        return true;
    }

    public List<Project> viewAllProjects() {
        return new ArrayList<>(allProjects);
    }

    public List<Project> viewCreatedProjects() {
        List<Project> mine = new ArrayList<>();
        for (Project p : allProjects) {
            if (p.getManagerInCharge() == this) {
                mine.add(p);
            }
        }
        return mine;
    }

    /* ===================================
       OFFICER REGISTRATION MANAGEMENT
       =================================== */

    public List<OfficerRegistration> viewOfficerRegistrations(Project project) {
        List<OfficerRegistration> regs = new ArrayList<>();
        for (OfficerRegistration r : allOfficerRegistrations) {
            if (r.getProject() == project) {
                regs.add(r);
            }
        }
        return regs;
    }

    public void approveOfficerRegistration(OfficerRegistration registration) {
        if (!allOfficerRegistrations.contains(registration)) {
            System.out.println("Approval failed: Registration not found.");
            return;
        }
        if (registration.getProject().getManagerInCharge() != this) {
            System.out.println("Approval failed: Not your project.");
            return;
        }
        registration.setStatus("Approved");
    }

    public void rejectOfficerRegistration(OfficerRegistration registration) {
        if (!allOfficerRegistrations.contains(registration)) {
            System.out.println("Rejection failed: Registration not found.");
            return;
        }
        if (registration.getProject().getManagerInCharge() != this) {
            System.out.println("Rejection failed: Not your project.");
            return;
        }
        registration.setStatus("Rejected");
    }

    /* ============================
       APPLICATION MANAGEMENT
       ============================ */

    public boolean approveApplication(Application application) {
        if (application == null || application.getStatus() != ApplicationStatus.Pending) {
            return false;
        }
        FlatType type = application.getFlatTypeChosen();
        Project project = application.getProject();
        if (type == FlatType.Two_Room && project.getTwoRoomUnits() > 0) {
            project.setTwoRoomUnits(project.getTwoRoomUnits() - 1);
        } else if (type == FlatType.Three_Room && project.getThreeRoomUnits() > 0) {
            project.setThreeRoomUnits(project.getThreeRoomUnits() - 1);
        } else {
            application.setStatus(ApplicationStatus.Unsuccessful);
            return false;
        }
        application.setStatus(ApplicationStatus.Successful);
        return true;
    }

    public boolean rejectApplication(Application application) {
        if (application == null || application.getStatus() != ApplicationStatus.Pending) {
            return false;
        }
        application.setStatus(ApplicationStatus.Unsuccessful);
        return true;
    }

    /* ============================
       WITHDRAWAL MANAGEMENT
       ============================ */

    public boolean approveWithdrawal(Application application) {
        if (application == null) return false;
        ApplicationStatus s = application.getStatus();
        Project project = application.getProject();
        FlatType type = application.getFlatTypeChosen();

        if (s == ApplicationStatus.Pending || s == ApplicationStatus.Booked) {
            if (s == ApplicationStatus.Booked) {
                if (type == FlatType.Two_Room) {
                    project.setTwoRoomUnits(project.getTwoRoomUnits() + 1);
                } else {
                    project.setThreeRoomUnits(project.getThreeRoomUnits() + 1);
                }
            }
            application.setStatus(ApplicationStatus.Withdrawn);
            return true;
        }
        return false;
    }

    public boolean rejectWithdrawal(Application application) {
        if (application == null || application.getStatus() != ApplicationStatus.WithdrawalRequested) {
            return false;
        }
        application.setStatus(ApplicationStatus.Booked);
        return true;
    }

    /* ============================
       REPORT GENERATION
       ============================ */

    public String generateFlatBookingReport(List<Application> applications) {
        StringBuilder report = new StringBuilder();
        for (Application app : applications) {
            if (app.getStatus() == ApplicationStatus.Booked) {
                report.append("Applicant: ").append(app.getApplicant().getNric())
                      .append(", Age: ").append(app.getApplicant().getAge())
                      .append(", Marital: ").append(app.getApplicant().getMaritalStatus())
                      .append(", Flat: ").append(app.getFlatTypeChosen())
                      .append(", Project: ").append(app.getProject().getProjectName())
                      .append(" (").append(app.getProject().getNeighborhood()).append(")\n");
            }
        }
        return report.toString();
    }

    /* ============================
       ENQUIRY MANAGEMENT
       ============================ */

    public List<Enquiry> viewAllEnquiries() {
        List<Enquiry> all = new ArrayList<>();
        for (Project p : allProjects) {
            all.addAll(p.getEnquiries());
        }
        return all;
    }

    public boolean replyToEnquiry(Enquiry enquiry, String replyText) {
        if (enquiry == null || replyText == null || replyText.trim().isEmpty()) {
            return false;
        }
        enquiry.addReply(enquiry.getEnquiryID(), replyText);
        enquiry.setRepliedBy(this);
        return true;
    }

    /* ===================================
       OPTIONAL DEMO MAIN METHOD
       =================================== */

    public static void main(String[] args) {
        HDBManager mgr = new HDBManager("S1234567A", "password", 40, "Married", new ArrayList<>());

        Project p = new Project("P001","Sunrise","Boon Lay","2-Room",
                                100,50, LocalDate.now().minusDays(1),
                                LocalDate.now().plusDays(30), mgr, 5);
        mgr.createProject(p);
        System.out.println("Created: " + p.getProjectName());

        p.setProjectName("Sunrise Updated");
        mgr.updateProject(p, p);
        System.out.println("Updated to: " + p.getProjectName());

        mgr.toggleProjectVisibility(p, false);
        System.out.println("Visible? " + p.isVisible());
    }
}
