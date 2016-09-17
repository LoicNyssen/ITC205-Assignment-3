package libary.LowLevelIntegration;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookDAOTest {
    
    int noBooks_ = 5;
    
    private IBookHelper _BookHelper;
    private IBook books_;
    
    BookMapDAO bookMapDAO_;
    

    @Before
    public void setUp() throws Exception {
        _BookHelper = new BookHelper();
        bookMapDAO_ = new BookMapDAO(_BookHelper);
    }

    // I was going to have this as 2 separate tests, but
    // I can't test list books with out adding books. So I 
    // Couldn't see the point of testing add book on its own. 
    // Prioritizing :)
    @Test
    public void testAddAndListBooks() {
        for (int i = 0; i < noBooks_ ; i++) {
            //arrange
            String author     = "Author " + i;
            String title      = "Title " + i;
            String callNumber = Integer.toString(i);
            
            //execute
            books_ = bookMapDAO_.addBook(author, title, callNumber);
            
            //assert
            assertEquals(author, books_.getAuthor());
            assertEquals(title, books_.getTitle());
            assertEquals(callNumber, books_.getCallNumber());
        }
        
        List<IBook> bookList = bookMapDAO_.listBooks();
        assertEquals(noBooks_, bookList.size());
    }

}
