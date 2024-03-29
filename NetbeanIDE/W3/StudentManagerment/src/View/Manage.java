package View;

import Model.Student;
import Common.Validate;
import View.Menu;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;

public class Manage{

    final Scanner sc = new Scanner(System.in);
    List<Student> std = new ArrayList<>();
    
    public void readDataFromFile(String filename) throws IOException, ParseException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String studentName = parts[1];
                    int semester = Integer.parseInt(parts[2]);
                    String courseName = parts[3];
                    if (Validate.isEmpIdValid(id)) {
                        Student st = new Student(studentName, semester, courseName, id);
                        std.add(st);
                    } else {
                        System.out.println("Typed wrong information format");
                    }
                }
            }
        }
    }

    public void saveDoctorsToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Student st : std) {
                writer.println(st.getId() + ","
                        + st.getStudentname() + ","
                        + st.getSemeter()
                        + "," + st.getCourse());

            }
        } catch (IOException e) {
            throw new IOException("Failed to save data to file: " + filename);
        }
    }

    public void createStudent() {
        try {

            do {
                System.out.print("Enter Student ID: ");
                String id = sc.nextLine();

                if (isStudentIdExists(id)) {
                    System.out.println("Student ID already exists. Please enter a different ID.");
                    continue;
                }

                System.out.print("Enter student name: ");
                String name = sc.nextLine();

                String course = courseMenu();

                System.out.print("Enter semeter: ");
                int semester = sc.nextInt();
                sc.nextLine();

                Student st = new Student(name, semester, course, id);
                std.add(st);
            } while (checkContinueAddingStudents());

            saveDoctorsToFile("student.txt");
            System.out.println("Doctor added successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private boolean checkContinueAddingStudents() {
        if (std.size() > 10) {
            System.out.println("Number of students is greater than 10.");
            System.out.print("Do you want to continue adding students? (Y/N): ");
            char continueAdding = sc.nextLine().charAt(0);
            return continueAdding == 'Y' || continueAdding == 'y';
        }
        return true;
    }

    private boolean isStudentIdExists(String id) {
        for (Student student : std) {
            if (student.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public String courseMenu() {
        String course = null;
        System.out.println("1. Java");
        System.out.println("2. .Net");
        System.out.println("3. C/C++");
        System.out.print("Enter your course: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                course = "Java";
                break;
            case 2:
                course = ".Net";
                break;
            case 3:
                course = "C/C++";
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return course;
    }

    public void FindAndSortStudent() {
        System.out.print("Enter the name or character of student you want to find: ");
        String partialName = sc.nextLine();
        List<Student> matchingStudents = new ArrayList<>();
        for (Student student : std) {

            if (student.getStudentname().toLowerCase().contains(partialName.toLowerCase())) {
                matchingStudents.add(student);
            }
        }

        // Sort the matching students by name
        Collections.sort(matchingStudents, Comparator.comparing(Student::getStudentname));

        // Display the results
        if (matchingStudents.isEmpty()) {
            System.out.println("No matching students found.");
        } else {
            System.out.println("Matching Students:");
            for (Student student : matchingStudents) {
                System.out.println("Name: " + student.getStudentname()
                        + ", Semester: " + student.getSemeter()
                        + ", Course Name: " + student.getCourse());
            }
        }
    }

    public void UpdateAndDeleteStudent() {
        System.out.println("Enter student ID to find: ");
        String searchId = sc.nextLine();

        Optional<Student> foundStudent = std.stream()
                .filter(student -> student.getId().equals(searchId))
                .findFirst();

        if (foundStudent.isPresent()) {
            System.out.println("Student found: " + foundStudent.get().getStudentname());
            System.out.println("Do you want to update (U) or delete (D) student?");
            String choice = sc.nextLine().toUpperCase();

            switch (choice) {
                case "U" ->
                    updateStudent(foundStudent.get());
                case "D" -> {
                    std.remove(foundStudent.get());
                    System.out.println("Student deleted successfully.");
                }
                default ->
                    System.out.println("Invalid choice.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    private void updateStudent(Student student) {
        System.out.println("Current student information:");
        System.out.println("Name: " + student.getStudentname());
        System.out.println("Semester: " + student.getSemeter());
        System.out.println("Course: " + student.getCourse());

        System.out.println("Do you want to update the information?");
        System.out.println("Enter Y for Yes, N for No:");
        String updateChoice = sc.nextLine().toUpperCase();

        if (updateChoice.equals("Y")) {
            System.out.println("Enter new information:");

            System.out.print("Enter new name: ");
            String newName = sc.nextLine();
            student.setStudentname(newName);

            System.out.print("Enter new semester: ");
            int newSemester = sc.nextInt();
            student.setSemeter(newSemester);

            sc.nextLine();
            String newCourse = courseMenu();
            student.setCourse(newCourse);

            System.out.println("Student information updated successfully.");
        } else {
            System.out.println("No updates were made.");
        }
    }

    public void displayAllStudents() {
        System.out.println("Student Information:");
        System.out.printf("%-15s%-15s%n", "Name", "Course");

        for (Student student : std) {
            System.out.printf("%-15s%-15s%n",
                    student.getStudentname(), student.getCourse());
        }
    }

    public void Report() {
        Map<String, Map<String, Integer>> studentCourseCounts = new HashMap<>();

        // Count occurrences of each unique combination of student name and course
        for (Student student : std) {
            String studentName = student.getStudentname();
            String course = student.getCourse();

            studentCourseCounts
                    .computeIfAbsent(studentName, k -> new HashMap<>())
                    .merge(course, 1, Integer::sum);
        }

        // Display the report
        System.out.println("");
        System.out.println("The report as below:");
        for (Map.Entry<String, Map<String, Integer>> entry : studentCourseCounts.entrySet()) {
            String studentName = entry.getKey();

            for (Map.Entry<String, Integer> courseCount : entry.getValue().entrySet()) {
                String course = courseCount.getKey();
                int totalCourses = courseCount.getValue();

                System.out.printf("%-13s | %-6s | %d%n", studentName, course, totalCourses);
            }
        }

    }
}
