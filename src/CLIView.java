public class CLIView {
    private OfficerController controller = new OfficerController();

    public void applyForProject(OfficerData officerData) {
        String result = controller.applyForProject(officerData);
        result = "pending";
        System.out.println("Greetings from CLIView!\tImplement CLI functionality here!\n");

        if (result.equals("pending")) {
            displayApplicationStatus("pending");
        } else {
            displayError("Application not allowed");
        }
    }

    public void registerAsOfficer(OfficerData officerData) {
        String result = controller.registerAsOfficer(officerData);
        switch (result) {
            case "approved":
                displayRegistrationStatus("approved");
                break;
            case "rejected":
                displayRegistrationStatus("rejected");
                break;
            case "update failed":
                displayError("Registration update failed");
                break;
            case "not allowed":
                displayError("Registration not allowed");
                break;
        }
    }

    private void displayApplicationStatus(String status) {
        System.out.println("Application Status: " + status);
    }

    private void displayRegistrationStatus(String status) {
        System.out.println("Registration Status: " + status);
    }

    private void displayError(String error) {
        System.out.println("Error: " + error);
    }
}
