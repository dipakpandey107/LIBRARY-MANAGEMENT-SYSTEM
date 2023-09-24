import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Scanner;

public class Student extends User implements FineCalculator {
    int studentID;
    ArrayList<Book> _book;

    Student(String userName, String passWord, String fullName, int studentID) {
        super(userName, passWord, fullName);
        this.studentID = studentID;
        _book = new ArrayList<>();
    }

    private static String getUserName(Student stud) {
        return stud.userName;
    }

    private static int getUserID(Student stud) {
        return stud.studentID;
    }

    private static String getFullName(Student stud) {
        return stud.fullName;
    }

    private static int getBookID(Book books) {
        return books.bookID;
    }

    private static String getBookName(Book books) {
        return books.bookName;
    }

    private static boolean getBookAvailability(Book books) {
        return books.available;
    }

    private static LocalDate getIssueDate(Book books) {
        return books.issueDate;
    }

    private static LocalDate getReturnDate(Book books) {
        return books.returnDate;
    }

    public void issueBook(ArrayList<Student> students, ArrayList<Book> book, Scanner scan) {
        if (!students.isEmpty()) {

            System.out.println("-----------------------------------");
            System.out.println("Enter student ID");
            int student_id = scan.nextInt();
            scan.nextLine();
            try {
                for (Student stud : students) {
                    if (student_id == stud.studentID) {
                        System.out.println("Enter book ID you want to issue");
                        int book_id = scan.nextInt();
                        scan.nextLine();
                        for (Book books : book) {
                            if (books.bookID == book_id) {
                                LocalDate date = LocalDate.now();
                                books.available = false;
                                books.issueDate = date;
                                books.returnDate = date.plusDays(15);
                                _book.add(new Book(getBookID(books), getBookName(books), getBookAvailability(books),
                                        date, date.plusDays(15)));

                                try {
                                    File inputFile = new File("book.csv");
                                    File tempFile = new File("temp.txt");

                                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                                    String currentLine;

                                    while ((currentLine = reader.readLine()) != null) {
                                        if (currentLine.contains(String.valueOf(getBookID(books)))) {
                                            continue;
                                        }
                                        writer.write(currentLine + System.getProperty("line.separator"));
                                    }

                                    writer.close();
                                    reader.close();
                                    inputFile.delete();
                                    tempFile.renameTo(inputFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // write new data

                                String[] data = new String[5];
                                data[0] = String.valueOf(getBookID(books));
                                data[1] = getBookName(books);
                                data[2] = String.valueOf(getBookAvailability(books));
                                data[3] = String.valueOf(getIssueDate(books));
                                data[4] = String.valueOf(getReturnDate(books));
                                try {
                                    FileWriter writer = new FileWriter("book.csv", true);
                                    writer.append(String.join(" ", data));
                                    writer.append("\n");
                                    writer.close();
                                    System.out.println("Data has been written to " + "book.csv");

                                    System.out.println("-----------------------------------");
                                } catch (IOException e) {
                                    System.out.println("An error occurred.");
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("No student data available");
        }

    }

    public void returnBook(ArrayList<Student> students, ArrayList<Book> book, Scanner scan) {
        if (!_book.isEmpty()) {
            System.out.println("Enter student ID");
            int student_id = scan.nextInt();
            scan.nextLine();
            for (Student stud : students) {
                if (student_id == stud.studentID) {

                System.out.println("-----------------------------------");
                    System.out.println("Enter book ID you want to return");
                    int book_id = scan.nextInt();
                    scan.nextLine();
                    try {
                        for (Book books : _book) {
                            if (books.bookID == book_id) {
                                int check = checkCharges(books);
                                if (check == 0) {

                                    // update object values

                                    books.available = true;
                                    books.issueDate = null;
                                    books.returnDate = null;
                                    try {

                                        File inputFile = new File("book.csv");
                                        File tempFile = new File("temp.txt");

                                        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                                        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                                        String currentLine;

                                        while ((currentLine = reader.readLine()) != null) {
                                            if (currentLine.contains(String.valueOf(getBookID(books)))) {
                                                continue;
                                            }
                                            writer.write(currentLine + System.getProperty("line.separator"));
                                        }

                                        writer.close();
                                        reader.close();
                                        inputFile.delete();
                                        tempFile.renameTo(inputFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    // write new data

                                    String[] data = new String[5];
                                    data[0] = String.valueOf(getBookID(books));
                                    data[1] = getBookName(books);
                                    data[2] = String.valueOf(getBookAvailability(books));
                                    data[3] = String.valueOf(getIssueDate(books));
                                    data[4] = String.valueOf(getReturnDate(books));
                                    try {
                                        FileWriter writer = new FileWriter("book.csv", true);
                                        writer.append(String.join(" ", data));
                                        writer.append("\n");
                                        writer.close();
                                        System.out.println("Data has been written to " + "book.csv");
                                    } catch (IOException e) {
                                        System.out.println("An error occurred.");
                                        e.printStackTrace();
                                    }

                                    // remove data from student arraylist
                                    _book.remove(books);

                                    System.out.println("Book return Successfully");
                                    break;

                                } else {

                                    Period period = Period.between(getReturnDate(books), LocalDate.now());
                                    int charges = period.getDays() * 5;

                                    System.out.println("Your payable charges are :" + charges);
                                    System.out.println("-----------------------------------");

                                    books.available = true;
                                    books.issueDate = null;
                                    books.returnDate = null;

                                    // update return book in main file
                                    try {
                                        File inputFile = new File("book.csv");
                                        File tempFile = new File("temp.txt");

                                        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                                        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                                        String currentLine;

                                        while ((currentLine = reader.readLine()) != null) {
                                            if (currentLine.contains(String.valueOf(getBookID(books)))) {
                                                continue;
                                            }
                                            writer.write(currentLine + System.getProperty("line.separator"));
                                        }

                                        writer.close();
                                        reader.close();
                                        inputFile.delete();
                                        tempFile.renameTo(inputFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    // write new data

                                    String[] data = new String[5];
                                    data[0] = String.valueOf(getBookID(books));
                                    data[1] = getBookName(books);
                                    data[2] = String.valueOf(getBookAvailability(books));
                                    data[3] = String.valueOf(getIssueDate(books));
                                    data[4] = String.valueOf(getReturnDate(books));
                                    try {
                                        FileWriter writer = new FileWriter("book.csv", true);
                                        writer.append(String.join(" ", data));
                                        writer.append("\n");
                                        writer.close();
                                        System.out.println("Data has been written to " + "book.csv");

                System.out.println("-----------------------------------");
                                    } catch (IOException e) {
                                        System.out.println("An error occurred.");
                                        e.printStackTrace();
                                    }

                                    _book.remove(books);
                                    System.out.println("Book returned Successfully");

                System.out.println("-----------------------------------");
                                    break;
                                }
                            } else if (_book.isEmpty()) {
                                System.out.println("No Books Issued");
                            } else {
                                System.out.println("Something Wrong");
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(e + "Array Index out of bound");
                    }
                } else {
                    System.out.println("Wrong ID");
                }
            }
        } else {
            System.out.println("No student data available");
        }
    }

    public void displayProfile(ArrayList<Student> students, Scanner scan) {
        System.out.println("Enter student ID");
        int student_id = scan.nextInt();
        scan.nextLine();
        for (Student stud : students) {
            if (stud.studentID == student_id) {
                System.out.println("Username is" + getUserName(stud));
                System.out.println("Student ID  is" + getUserID(stud));
                System.out.println("Full Name is" + getFullName(stud));
                System.out.println("-----------------------------------");
                System.out.println("Books Issued are");
                for (Book books : _book) {
                    System.out.println("-----------------------------------");

                    try {
                        File inputFile = new File("book.csv");
                        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

                        String currentLine;

                        while ((currentLine = reader.readLine()) != null) {
                            if (currentLine.contains(String.valueOf(getBookID(books)))) {
                                System.out.println(currentLine);
                                // Or, store the line in a data structure of your choice.
                            }
                        }

                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // System.out.println("Book ID :" + books.bookID);
                    // System.out.println("Book Name :" + books.bookName);
                    // System.out.println("Book Name :" + books.bookName);
                    // System.out.println("Book Issued Date :" + books.issueDate);
                    // System.out.println("Book Return Date :" + books.returnDate);
                    System.out.println("-----------------------------------");
                }
            }
        }
    }

    public int checkCharges(Book books) {
        LocalDate date = LocalDate.now();
        System.out.println(books.returnDate);
        System.out.println(books.returnDate.compareTo(date));
        if (books.returnDate.compareTo(date) <= 15) {
            return 0;
        } else {
            return 1;
        }
    }

}
