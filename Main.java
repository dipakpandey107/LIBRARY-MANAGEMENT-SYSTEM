import java.util.Scanner;
import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Main{

    private String csvFile = "book.csv";
    private String[] headers = { "ID", "Name", "Available", "Issue", "Return" };

    Scanner scan = new Scanner(System.in);
    Console cnsl = System.console();

    private String userType = "";
    Admin currentAdmin;
    Student currentStudent;

    private boolean addHeaders = true;

    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Admin> admin = new ArrayList<>();
    private ArrayList<Book> book = new ArrayList<>();

    // Function for User Registration.
    public void register(String userType) {
        System.out.println("Enter Username: ");
        String uname = scan.nextLine();
        System.out.print("Enter Password: ");
        String passwd = String.valueOf(cnsl.readPassword());

        for (int i = 0; i < passwd.length(); i++)
            System.out.print("*");

        System.out.println("\nEnter FullName: ");
        String fullName = scan.nextLine();

        if (userType.equals("Student")) {
            System.out.println("Enter StudentID: ");
            int stdID = scan.nextInt();
            scan.nextLine();
            students.add(new Student(uname, passwd, fullName, stdID));
        } else if (userType.equals("Admin")) {
            System.out.println("Enter AdminID: ");
            int adminID = scan.nextInt();
            admin.add(new Admin(uname, passwd, fullName, adminID));
        }
        System.out.println("\nAccount Created Successfully\n");
    }

    // Login Function
    public int signIn(String userType) {
        System.out.println("-------------Sign-In---------------");
        System.out.print("Enter Username: ");
        String uname = scan.nextLine();
        System.out.print("Enter Password: ");
        String passwd = String.valueOf(cnsl.readPassword());
        for (int i = 0; i < passwd.length(); i++)
            System.out.print("*");
        System.out.println();

        // Student Login
        if (userType.equals("Student")) {
            return studentSignIn(uname, passwd);
        } else if (userType.equals("Admin")) {
            return adminSignIn(uname, passwd);
        } else {
            return -1;
        }
    }

    // Student Login
    public int studentSignIn(String uname, String passwd) {
        for (Student student : students) {
            if (student.userName.equals(uname) && student.passWord.equals(passwd)) {
                // If Authorization Successful.
                currentStudent = student;
                return 1;
            } else if (student.userName.equals(uname)) {
                // If Wrong Password.
                return 0;
            }
        }
        // Authorization Failed.
        return -1;
    }

    // Teacher Login
    public int adminSignIn(String uname, String passwd) {
        for (Admin teacher : admin) {
            if (teacher.userName.equals(uname) && teacher.passWord.equals(passwd)) {
                // If Authorization Successful.
                currentAdmin = teacher;
                return 1;
            } else if (teacher.userName.equals(uname)) {
                // If Wrong Password.
                return 0;
            }
        }
        // Authorization Failed.
        return -1;
    }

    // Function to Reset Password.
    public int resetPassword(String userType) {
        System.out.println("----------Reset-Password-----------");
        if (userType.equals("Student"))
            return resetPasswordStudent();
        else if (userType.equals("Admin"))
            return resetPasswordAdmin();

        // Invalid Option
        return -1;
    }

    // Reset Password For Student.
    public int resetPasswordStudent() {
        System.out.println("Enter Username: ");
        String uname = scan.nextLine();
        for (Student student : students) {
            if (student.userName.equals(uname)) {
                System.out.print("Enter New Password: ");
                String passwd = String.valueOf(cnsl.readPassword());
                for (int i = 0; i < passwd.length(); i++)
                    System.out.print("*");
                System.out.println();
                student.passWord = passwd;
                // Successful change of password.
                return 1;
            }
        }
        // If User Doesn't Exist.
        return 0;
    }

    // Reset Password For Teacher.
    public int resetPasswordAdmin() {
        System.out.println("Enter Username: ");
        String uname = scan.nextLine();
        for (Admin teacher : admin) {
            if (teacher.userName.equals(uname)) {
                System.out.print("Enter New Password: ");
                String passwd = String.valueOf(cnsl.readPassword());
                for (int i = 0; i < passwd.length(); i++)
                    System.out.print("*");
                System.out.println();
                teacher.passWord = passwd;
                // Successful change of password.
                return 1;
            }
        }
        // If User Doesn't Exist.
        return 0;
    }

    // Common Function to take input.
    public int takeInput(String state) {
        int opt;

        if (state.equals("Password")) {
            System.out.println("----------Wrong Password-----------");
            System.out.println("1. Reset Password\n2. Retry");
            opt = scan.nextInt();
            scan.nextLine();
            System.out.println("-----------------------------------");
            return opt;
        } else if (state.equals("home")) {
            if (userType.equals("Student"))
                System.out.println("--------------Student--------------");
            else
                System.out.println("---------------Admin---------------");

            System.out.println("Enter Option: \n1. Sign In\n2. Register\n3. Select User Type");
            opt = scan.nextInt();
            scan.nextLine();
            System.out.println("-----------------------------------");
            return opt;
        } else if (state.equals("type")) {
            System.out.println("----------Select User Type----------");
            System.out.println("1. Student\n2. Admin\n3. Exit");
            opt = scan.nextInt();
            scan.nextLine();
            System.out.println("-----------------------------------");
            return opt;
        }
        return -1;
    }

    // Show Student Options
    public void studentOptions() {
        System.out.println("-----------Select Option-----------");
        int opt = 0;
        while (opt != 4) {
            System.out.println("1. Issue Book: \n2. Return Book: \n3. Display Profile  \n4. Logout");
            opt = scan.nextInt();
            scan.nextLine();
            if (opt == 1) {
                currentStudent.issueBook(students, book, scan);
            } else if (opt == 2) {
                currentStudent.returnBook(students, book, scan);
            } else if (opt == 3) {
                currentStudent.displayProfile(students, scan);
            } else {
                System.out.println("Invalid Option");
            }
        }
        System.out.println("-----------------------------------");
    }

    // Show Teacher Options
    public void adminOptions() {
        int opt = 0;
        System.out.println("-----------Select Option-----------");
        System.out.println("1. Add Book: \n2.Logout");
        opt = scan.nextInt();
        scan.nextLine();
        if (opt == 1) {
            addBook();
        } else {
            System.out.println("Enter valid option");
        }
        System.out.println("-----------------------------------");
    }

    public void addBook() {
        int opt;
        int bookID = 0;
        String bookName = "";
        boolean available = false;
        while (true) {
            System.out.println("-----------Select Option-----------");
            System.out.println("1. New Book: \n2.Logout");
            opt = scan.nextInt();
            scan.nextLine();
            if (opt == 1) {
                System.out.println("-----------------------------------");
                System.out.println("Enter book ID");
                bookID = scan.nextInt();
                scan.nextLine();
                System.out.println("Enter book Name");
                bookName = scan.nextLine();
                available = true;
                book.add(new Book(bookID, bookName, available));
                System.out.println("-----------------------------------");

            } else {
                break;
            }
        }
        if (!book.isEmpty()) {
            for (Book books : book) {
                String[] data = new String[5];
                data[0] = String.valueOf(books.bookID);
                data[1] = books.bookName;
                data[2] = String.valueOf(books.available);
                try {
                    FileWriter writer = new FileWriter(csvFile, true);

                    // Write headers
                    if (addHeaders) {
                        for (int i = 0; i < headers.length; i++) {
                            writer.append(headers[i]);
                            if (i != headers.length - 1) {
                                writer.append(" ");
                            }
                        }
                        writer.append("\n");
                        addHeaders = false;
                    }

                    // Write data to CSV file
                    writer.append(String.join(" ", data));
                    writer.append("\n");
                    writer.close();
                    System.out.println("Data has been written to " + csvFile);
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }
        System.out.println("-----------------------------------");
    }

    public static void main(String[] args) {
        Main site = new Main();
        
        while (true) {
            // Select userType
            int opt = site.takeInput("type");

            if (opt == 1)
                site.userType = "Student";
            else if (opt == 2)
                site.userType = "Admin";
            else
                break;

            while (true) {
                opt = site.takeInput("home");
                // Sign-In
                if (opt == 1) {
                    opt = site.signIn(site.userType);

                    // Login Successful

                    if (opt == 1) {
                        System.out.println("\nSign-In Successful\n");
                        if (site.userType == "Student") {
                            site.studentOptions();
                        } else if (site.userType == "Admin") {
                            site.adminOptions();
                        }
                    }

                    // Wrong Password
                    else if (opt == 0) {
                        opt = site.takeInput("Password");

                        // Reset Password
                        if (opt == 1) {
                            int res = site.resetPassword(site.userType);
                            if (res == 1) {
                                System.out.println("Password Changed Successfully");
                            } else {
                                System.out.println("User Doesn't Exist");
                                continue;
                            }
                        }

                        // Retry
                        else if (opt == -1) {
                            continue;
                        }
                    }

                    // User Doesn't Exist
                    else if (opt == -1) {
                        System.out.println("User Doesn't Exist");
                    }

                }

                // Register
                else if (opt == 2) {
                    site.register(site.userType);
                }

                // Exit
                else if (opt == 3) {
                    break;
                } else {
                    System.out.println("Not a Valid Option");
                }
            }
        }
    }
}
