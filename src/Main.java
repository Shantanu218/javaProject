//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Hello from Main.java!\n");

            OfficerData officer = new OfficerData("John Doe");
            CLIView view = new CLIView();

            view.applyForProject(officer);
            view.registerAsOfficer(officer);

    }
}