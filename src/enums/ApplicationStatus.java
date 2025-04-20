package enums;

public enum ApplicationStatus {
    Pending,
    Successful,
    Unsuccessful,   // <-- fixed here
    Booked,
    Withdrawn       // <-- add this if you need to represent a withdrawn application
}
