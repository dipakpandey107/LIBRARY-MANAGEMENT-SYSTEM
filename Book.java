import java.time.LocalDate;

public class Book {
    int bookID;
    String bookName;
    boolean available;
    LocalDate issueDate,returnDate;
    float charges;
    Book(int bookID,String bookName,boolean available){
        this.bookID = bookID;
        this.bookName = bookName;
        this.available = available;
        issueDate = null;
        returnDate = null;
        charges = 0;
    }
    Book(int bookID,String bookName,boolean available,LocalDate issue,LocalDate returns){
        this.bookID = bookID;
        this.bookName = bookName;
        this.available = available;
        issueDate = issue;
        returnDate = returns;
        this.charges = 0;
    }
}
