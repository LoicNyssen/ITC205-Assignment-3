package library.LowLevelIntegration;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookDAOTest {
    
    private int noAuthors_ = 4;
    private int noBooks_   = 5;
    
    private int totalBooks_ = noAuthors_ * noBooks_;
    
    private IBookHelper bookHelper_;
    
    private BookMapDAO bookMapDAO_;
    
    private String author_[]     = new String[totalBooks_];
    private String title_[]      = new String[totalBooks_];
    private String callNumber_[] = new String[totalBooks_];
    

    @Before
    public void setUp() throws Exception {
        setUpTestData();
    }
    
    
    
    public void setUpTestData() {
        bookHelper_ = new BookHelper();
        bookMapDAO_ = new BookMapDAO(bookHelper_);
        
        int i = 0;
        
        for (int a = 0; a < noAuthors_ ; a++) {
            for (int b = 0; b < noBooks_ ; b++) {
                author_[i]  = "Author " + a;
                title_[i]   = "Title " + b;
                callNumber_[i] = Integer.toString(i);
                
                bookMapDAO_.addBook(author_[i], title_[i], callNumber_[i]);
                
                i++;
            }
        }
    }

    
    
    @Test
    public void testListBooks() {
        List<IBook> bookList = bookMapDAO_.listBooks();

        assertEquals(totalBooks_, bookList.size());
    }
    
    
    
    @Test
    public void testFindBooksByAuthor() {
        //arrange
        String author = "Author 1";
        
        //execute
        List<IBook> books = bookMapDAO_.findBooksByAuthor(author);
        for (IBook book : books) {
            String actual = book.getAuthor();
        
            //assert
            assertEquals (author, actual);
        }
        assertEquals(noBooks_, books.size());
    }

    
    
    @Test
    public void testFindBooksByTitle() {
        //arrange
        String title = "Title 1";
        
        //execute
        List<IBook> books = bookMapDAO_.findBooksByTitle(title);
        for (IBook book : books) {
            String actual = book.getTitle();
        
            //assert
            assertEquals (title, actual);
        }
        assertEquals(noAuthors_, books.size());
    }
    
    
    
    @Test
    public void testFindBooksByAuthorTitle() {
        //arrange
        String author = "Author 1";
        String title  = "Title 1";
        
        int noBooksExpected = 1;
        
        //execute
        List<IBook> books = bookMapDAO_.findBooksByAuthorTitle(author, title);
        
        //assert
        assertEquals(noBooksExpected, books.size());
    }
}
