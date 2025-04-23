import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import enums.EnquiryStatus;

/**
 * Represents a project enquiry submitted by an applicant.
 */
public class Enquiry {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    private final int enquiryID;
    private final Applicant applicant;
    private final Project project;

    private String enquiryText;
    private final LocalDateTime dateSubmitted;

    private String replyText;
    private LocalDateTime replyDate;
    private User repliedBy;

    private EnquiryStatus status;

    public Enquiry(Applicant applicant, Project project, String enquiryText) {
        this.enquiryID     = ID_GEN.getAndIncrement();
        this.applicant     = applicant;
        this.project       = project;
        this.enquiryText   = enquiryText;
        this.dateSubmitted = LocalDateTime.now();
        this.status        = EnquiryStatus.Pending;

        // link back into the applicant's list and the project's list
        applicant.getEnquiries().add(this);
        project.addEnquiry(this);
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

    public LocalDateTime getDateSubmitted() {
        return dateSubmitted;
    }

    public String getReplyText() {
        return replyText;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public User getRepliedBy() {
        return repliedBy;
    }

    public EnquiryStatus getStatus() {
        return status;
    }

    /** Edit the enquiry text. */
    public void edit(String newText) {
        if (status == EnquiryStatus.Replied) {
            throw new IllegalStateException("Cannot edit after a reply");
        }
        this.enquiryText = newText;
    }

    /** Reply to this enquiry. */
    public void reply(String replyText, User replier) {
        if (status == EnquiryStatus.Replied) {
            throw new IllegalStateException("Already replied");
        }
        this.replyText   = replyText;
        this.replyDate   = LocalDateTime.now();
        this.repliedBy   = replier;
        this.status      = EnquiryStatus.Replied;
    }

    /** Delete this enquiry from both applicant and project. */
    public void delete() {
        applicant.getEnquiries().remove(this);
        project.getEnquiries().remove(this);
    }

    @Override
    public String toString() {
        return String.format(
            "[%d] %s → \"%s\" (%s)%s",
            enquiryID,
            applicant.getNric(),
            enquiryText,
            status,
            (status == EnquiryStatus.Replied ? " Replied: \"" + replyText + "\"" : "")
        );
    }
}
