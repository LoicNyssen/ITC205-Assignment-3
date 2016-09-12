package library.entities;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;


public class BookTest {
    private String author_;
    private String title_;
    private String callNumber_;
    private int id_;
    private Book book_; 

    @Before
    public void setUp() throws Exception {
        author_ = "Author";
        title_ = "title";
        callNumber_ = "0";
        id_ = 1;
        
        book_ = new Book(author_, title_, callNumber_, id_);
    }
    
    @After
    public void tearDown() throws Exception {
        book_ = null;
    }
    
    //Tests if book instance is an instance of IBook
    @Test
    public void testBook(){
        //arrange
        IBook actual = new Book(author_, title_, callNumber_, id_);  
     
        //assert
        assertNotNull(actual);
        assertTrue(actual instanceof IBook);
    }
    
    //Test if Author is set correctly
    @Test
    public void testBookAuthorSets() {
        //arrange
        String expected = author_;
        String actual = book_.getAuthor();
        
        //assert
        assertEquals(expected, actual);
    }
    
    //Test if Title is set correctly
    @Test
    public void testBookTitleSets() {
        //arrange
        String expected = title_;
        String actual = book_.getTitle();
        
        //assert
        assertEquals(expected, actual);
    }
    
    //Test if CallNumber is set correctly
    @Test
    public void testBookCallNumberSets() {
        //arrange
        String expected = callNumber_;
        String actual = book_.getCallNumber();
        
        //assert
        assertEquals(expected, actual);
    }
    
    //Test if ID is set correctly
    @Test
    public void testBookIDSets() {
        //arrange
        int expected = id_;
        int actual = book_.getID();
        
        //assert
        assertEquals(expected, actual);
    }
    
    //Test if Exception thrown if Author is Null
    @Test(expected=IllegalArgumentException.class)
    public void testBookNullAuthorPassed() {
        //arrange
        new Book(null, title_, callNumber_, id_);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    //Test if Exception thrown if Author is Empty
    @Test(expected=IllegalArgumentException.class)
    public void testBookEmptyAuthorPassed() {
        //arrange
        new Book("", title_, callNumber_, id_);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    //Test if Exception thrown if Title is Null
    @Test(expected=IllegalArgumentException.class)
    public void testBookNullTitlePassed() {
        //arrange
        new Book(author_, null, callNumber_, id_);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    //Test if Exception thrown if Title is Empty
    @Test(expected=IllegalArgumentException.class)
    public void testBookEmptyTitlePassed() {
        //arrange
        new Book(author_, "", callNumber_, id_);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    //Test if Exception thrown if CallNumber is Null
    @Test(expected=IllegalArgumentException.class)
    public void testBookNullCallNumberPassed() {
        //arrange
        new Book(author_, title_, null, id_);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    //Test if Exception thrown if CallNumber is Empty
    @Test(expected=IllegalArgumentException.class)
    public void testBookEmptyCallNumberPassed() {
        //arrange
        new Book(author_, title_, "", id_);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    //Test if Exception thrown if CAllnumber is Empty
    @Test(expected=IllegalArgumentException.class)
    public void testBookIncorrectIdPassed() {
        //arrange
        new Book(author_, title_, callNumber_, 0);
        
        //assert
        fail("Should have thrown a IllegalArgumentException");
    }
    
    //Test if exception thrown is book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testBorrowException () {
        //arrange
        ILoan mockLoan = mock(ILoan.class);
        book_.setState(EBookState.DAMAGED);
        
        //execute
        book_.borrow(mockLoan);
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    //Tests if book's loan variable is the same to the actual loan
    //and test book state.
    @Test
    public void testBorrow() {
        //arrange
        ILoan mockLoan = mock(ILoan.class);
        EBookState expectedState = EBookState.ON_LOAN;

        //execute
        book_.borrow(mockLoan);
        EBookState actualState = book_.getState();
        ILoan actualLoan = book_.getLoan();
        
        //assert
        assertEquals(expectedState, actualState);
        assertNotNull(actualLoan);
        assertEquals(mockLoan, actualLoan);
    }
    
    //Test if getLoan returns correct loan.
    @Test
    public void testGetLoan() {
        //arrange
        ILoan expected = mock(ILoan.class);
        
        //execute
        book_.borrow(expected);
        ILoan actual = book_.getLoan();
        
        //assert
        assertEquals(expected, actual);
    }
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testReturnBookException () {
        //execute
        book_.returnBook(false);
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    //Test returnBook with damaged book  
    @Test
    public void testReturnBookDamaged () {
        //arrange
        EBookState expected = EBookState.DAMAGED;
        boolean isdamaged = true;
        
        book_.borrow(mock(ILoan.class));
        
        //execute
        book_.returnBook(isdamaged);
        
        //assert
        EBookState actual = book_.getState();
        assertEquals(expected, actual);
    }
    
    //Test returnBook with damaged book  
    @Test
    public void testReturnBookNotDamaged () {
        //arrange
        EBookState expected = EBookState.AVAILABLE;
        boolean isdamaged = false;
        
        book_.borrow(mock(ILoan.class));
        
        //execute
        book_.returnBook(isdamaged);
        
        //assert
        EBookState actual = book_.getState();
        assertEquals(expected, actual);
    }
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testLoseException () {
        //execute
        book_.lose();
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    //Test if correct state set if book is lost.
    @Test
    public void testLose() {
        //arrange
        EBookState expected = EBookState.LOST;
        book_.borrow(mock(ILoan.class));
        
        //execute
        book_.lose();
        
        //assert
        EBookState actual = book_.getState();
        assertEquals(expected, actual);     
    }
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testRepairException () {
        //execute
        book_.repair();
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    //Test if correct state set if book is lost.
    @Test
    public void testRepair() {
        //arrange
        EBookState expected = EBookState.AVAILABLE;
        
        book_.borrow(mock(ILoan.class));    //Maybe these 2 lines should
        book_.returnBook(true);             //be replaced with setState???
        
        //execute
        book_.repair();
        
        //assert
        EBookState actual = book_.getState();
        assertEquals(expected, actual);     
    }
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testDisposeException () {
        //arrange
        book_.setState(EBookState.ON_LOAN);
        
        //execute
        book_.dispose();
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    //Test if exception thrown if book is in incorrect state
    @Test
    public void testDispose () {
        //arrange
        EBookState expected = EBookState.DISPOSED;
        
        //execute
        book_.dispose();
        
        //assert
        EBookState actual = book_.getState();
        assertEquals(expected, actual); 
    }
}
