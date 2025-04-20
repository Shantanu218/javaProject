import java.util.Date;

/**
 * Represents a project enquiry submitted by an applicant.
 */
public class Enquiry {
    private static int nextId = 1;

    private final int enquiryID;
    private final Applicant applicant;
    private final Project project;
    private String enquiryText;
    private final Date dateSubmitted;
    private String reply;
    private Date replyDate;
    private User repliedBy;
    private String status;  // "Pending" or "Replied"

    /**
     * Constructs a new Enquiry, assigns a unique ID, and registers it
     * with both the applicant and the project.
     *
     * @param applicant   the applicant making the enquiry
     * @param project     the project the enquiry is about
     * @param enquiryText the enquiry content
     */
    public Enquiry(Applicant applicant, Project project, String enquiryText) {
        this.enquiryID = nextId++;
        this.applicant = applicant;
        this.project = project;
        this.enquiryText = enquiryText;
        this.dateSubmitted = new Date();
        this.reply = "";
        this.replyDate = null;
        this.repliedBy = null;
        this.status = "Pending";

        // Automatically add to applicant's and project's lists
        applicant.getEnquiries().add(this);
        project.getEnquiries().add(this);
    }

    public int getEnquiryID() {
        return enquiryID;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public Project getProject() {
        return project;
    }

    public String getEnquiryText() {
        return enquiryText;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public String getReply() {
        return reply;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public User getRepliedBy() {
        return repliedBy;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Edits this enquiry’s text if the ID matches.
     *
     * @param enquiryID the ID to match
     * @param newText   the new enquiry text
     */
    public void editEnquiry(int enquiryID, String newText) {
        if (this.enquiryID == enquiryID) {
            this.enquiryText = newText;
        }
    }

    /**
     * Deletes this enquiry from both the applicant and project lists if the ID matches.
     *
     * @param enquiryID the ID to match
     */
    public void deleteEnquiry(int enquiryID) {
        if (this.enquiryID == enquiryID) {
            applicant.getEnquiries().remove(this);
            project.getEnquiries().remove(this);
        }
    }

    /**
     * Adds a reply to this enquiry if the ID matches, setting reply text, date, status.
     *
     * @param enquiryID the ID to match
     * @param replyText the reply content
     */
    public void addReply(int enquiryID, String replyText) {
        if (this.enquiryID == enquiryID) {
            this.reply = replyText;
            this.replyDate = new Date();
            this.status = "Replied";
            // Note: repliedBy should be set by the caller (e.g., HDBManagerMenu),
            // for example via enquiry.setRepliedBy(currentUser).
        }
    }

    /**
     * Optionally allow setting who replied.
     */
    public void setRepliedBy(User user) {
        this.repliedBy = user;
    }
}
