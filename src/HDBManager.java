import enums.FlatType;
import enums.ApplicationStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * HDBManager is responsible for managing BTO projects, officer registrations,
 * application approvals/withdrawals, report generation, and enquiry replies.
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
        if (!allProjects.contains(oldProject) || oldProject.getManagerInCharge() != this) {
            System.out.println("Update failed: Unauthorized or not found.");
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
        if (!allProjects.contains(project) || project.getManagerInCharge() != this) {
            System.out.println("Delete failed: Unauthorized or not found.");
            return false;
        }
        allProjects.remove(project);
        return true;
    }

    public boolean toggleProjectVisibility(Project project, boolean visibility) {
        if (!allProjects.contains(project) || project.getManagerInCharge() != this) {
            System.out.println("Toggle failed: Unauthorized or not found.");
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
        if (allOfficerRegistrations.contains(registration)
                && registration.getProject().getManagerInCharge() == this) {
            registration.setStatus("Approved");
        } else {
            System.out.println("Approval failed: Unauthorized or not found.");
        }
    }

    public void rejectOfficerRegistration(OfficerRegistration registration) {
        if (allOfficerRegistrations.contains(registration)
                && registration.getProject().getManagerInCharge() == this) {
            registration.setStatus("Rejected");
        } else {
            System.out.println("Rejection failed: Unauthorized or not found.");
        }
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

        boolean success = false;
        if (type == FlatType.Two_Room && project.getTwoRoomUnits() > 0) {
            project.setTwoRoomUnits(project.getTwoRoomUnits() - 1);
            success = true;
        } else if (type == FlatType.Three_Room && project.getThreeRoomUnits() > 0) {
            project.setThreeRoomUnits(project.getThreeRoomUnits() - 1);
            success = true;
        }

        application.setStatus(success ? ApplicationStatus.Successful : ApplicationStatus.Unsuccessful);
        return success;
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
        if (application == null || !application.isWithdrawalRequested()) {
            return false;
        }

        ApplicationStatus current = application.getStatus();
        Project project = application.getProject();
        FlatType type = application.getFlatTypeChosen();

        // If previously booked, return the unit
        if (current == ApplicationStatus.Booked) {
            if (type == FlatType.Two_Room) {
                project.setTwoRoomUnits(project.getTwoRoomUnits() + 1);
            } else {
                project.setThreeRoomUnits(project.getThreeRoomUnits() + 1);
            }
        }

        // Mark as withdrawn
        application.setStatus(ApplicationStatus.Withdrawn); // add Withdrawn to enum if needed
        return true;
    }

    /* ===================================================== 
       Reject a Withdrawal Request
    ===================================================== */
    public boolean rejectWithdrawal(Application application) {
        if (application == null || !application.isWithdrawalRequested()) {
            return false;
        }
        // Simply clear the withdrawal request flag; leave status as-is (Pending or Booked)
        application.setWithdrawalRequest(false);
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
}
