
public class Main {

    public static void main(String[] args) {
        Library l = new Library();
        ObjDao dao = new ObjDao();
        ObjView view = new ObjView();
        ObjController controller = new ObjController(dao, view);
        while (true) {
            view.ObjMenu()
            int choice = l.getInt("Your choice: ");
            switch (choice) {
                case 1: controller.A; break;
                case 2: controller.B; break;
                case 3: controller.C; break;
                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
            }
        }
    }
}
