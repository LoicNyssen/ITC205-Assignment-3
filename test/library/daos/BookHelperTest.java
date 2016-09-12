package library.daos;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.entities.Book;
import library.interfaces.entities.IBook;

public class BookHelperTest {
    private String author_;
    private String title_;
    private String callNumber_;
    private int id_;
    
    private BookHelper sut_;
    
    @Before
    public void setUp() throws Exception {
        author_ = "Author";
        title_ = "title";
        callNumber_ = "0";
        id_ = 1;
        
        sut_ = new BookHelper();
    }

    @After
    public void tearDown() throws Exception {
        sut_ = null;
    }
    
    //Test if object created is instance of book
    //and check if author, title, callNumber and id is passed correctly. 
    @Test
    public void testmakeBook() {
        //execute
        IBook actual = sut_.makeBook(author_, title_, callNumber_, id_);
        
        //assert
        assertTrue(actual instanceof IBook);
        
        assertEquals(actual.getAuthor(), author_);
        assertEquals(actual.getTitle(), title_);
        assertEquals(actual.getCallNumber(), callNumber_);
        assertEquals(actual.getID(), id_);
    }

}
