
import Controller.Controller;
import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        try {
            controller.readDataFromFile("student.txt");
        } catch (IOException | ParseException e) {
            System.out.println("Error");
        }
        controller.run();
    }
}
//xet den truong hop trung id nhung khac ten

