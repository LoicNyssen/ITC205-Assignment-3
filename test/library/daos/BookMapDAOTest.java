package library.daos;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.entities.Book;
import library.interfaces.entities.IBook;

public class BookMapDAOTest {
    
    private BookMapDAO sut_;
    private BookHelper bookHelper_;

    @Before
    public void setUp() throws Exception {
        bookHelper_ = mock(BookHelper.class);
        sut_ = new BookMapDAO(bookHelper_);
    }

    
    
    @After
    public void tearDown() throws Exception {
        sut_ = null;
    }
    
    
    
    //Test if exception is thrown if null is passed.
    @Test(expected=IllegalArgumentException.class)
    public void testBookDAOException() {
        //arrange
        sut_ = new BookMapDAO(null);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    
    
    //Test if book is added correctly. 
    @Test
    public void testAddBook() {
        //arrange
        String author = "Author";
        String title  = "title";
        String callNo = "0";
        int id = 1;
        
        IBook expected = new Book(author, title, callNo, id);
        when(bookHelper_.makeBook(author, title, callNo, id)).thenReturn(expected);
        
        //execute
        IBook actual = sut_.addBook(author, title, callNo);
        
        //assert
        assertEquals(expected, actual);
        verify(bookHelper_).makeBook(author, title, callNo, id);
    }
    
    
    
    //Test if list of books returned correctly. 
    @Test
    public void testGetBookByID() {
        //arrange
        String author = "Author";
        String title  = "title";
        String callNo = "0";
        int id = 1;
        
        IBook expected = new Book(author, title, callNo, id);
        when(bookHelper_.makeBook(author, title, callNo, id)).thenReturn(expected);
        
        sut_.addBook(author, title, callNo);
        
        //execute
        IBook actual = sut_.getBookByID(id);
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if null returned correctly. 
    @Test
    public void testGetBookByIDNonexistentID() {
        //arrange
        IBook expected = null;
        
        //execute
        IBook actual = sut_.getBookByID(2);
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if list of books returned correctly. 
    @Test
    public void testListBooks() {
        //arrange
        String author = "Author";
        String title  = "title";
        String callNo = "0";
        int id = 1;
        
        IBook expected = new Book(author, title, callNo, id);
        when(bookHelper_.makeBook(author, title, callNo, id)).thenReturn(expected);
        
        sut_.addBook(author, title, callNo);
        
        //execute
        IBook actual = sut_.listBooks().get(0);
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if list of books returned correctly. 
    @Test
    public void testFindBooksByAuthor() {
        //arrange
        String author = "Author";
        String title  = "title";
        String callNo = "0";
        int id = 1;
        
        IBook expected = new Book(author, title, callNo, id);
        when(bookHelper_.makeBook(author, title, callNo, id)).thenReturn(expected);
        
        sut_.addBook(author, title, callNo);
        
        //execute
        IBook actual = sut_.findBooksByAuthor(author).get(0);
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if list of books returned correctly. 
    @Test
    public void testFindBooksByTitle() {
        //arrange
        String author = "Author";
        String title  = "title";
        String callNo = "0";
        int id = 1;
        
        IBook expected = new Book(author, title, callNo, id);
        when(bookHelper_.makeBook(author, title, callNo, id)).thenReturn(expected);
        
        sut_.addBook(author, title, callNo);
        
        //execute
        IBook actual = sut_.findBooksByTitle(title).get(0);
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if list of books returned correctly. 
    @Test
    public void testFindBooksByAuthorTitle() {
        //arrange
        String author = "Author";
        String title  = "title";
        String callNo = "0";
        int id = 1;
        
        IBook expected = new Book(author, title, callNo, id);
        when(bookHelper_.makeBook(author, title, callNo, id)).thenReturn(expected);
        
        sut_.addBook(author, title, callNo);
        
        //execute
        IBook actual = sut_.findBooksByAuthorTitle(author, title).get(0);
        
        //assert
        assertEquals(expected, actual);
    }
}
