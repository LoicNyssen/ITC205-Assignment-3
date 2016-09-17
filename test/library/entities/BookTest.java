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
    private Book sut_; 

    @Before
    public void setUp() throws Exception {
        author_     = "Author";
        title_      = "title";
        callNumber_ = "0";
        id_         = 1;
        
        sut_ = new Book(author_, title_, callNumber_, id_);
    }
    
    
    
    @After
    public void tearDown() throws Exception {
        sut_ = null;
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
        String actual   = sut_.getAuthor();
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if Title is set correctly
    @Test
    public void testBookTitleSets() {
        //arrange
        String expected = title_;
        String actual   = sut_.getTitle();
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if CallNumber is set correctly
    @Test
    public void testBookCallNumberSets() {
        //arrange
        String expected = callNumber_;
        String actual   = sut_.getCallNumber();
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if ID is set correctly
    @Test
    public void testBookIDSets() {
        //arrange
        int expected = id_;
        int actual   = sut_.getID();
        
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
        sut_.setState(EBookState.DAMAGED);
        
        //execute
        sut_.borrow(mockLoan);
        
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
        sut_.borrow(mockLoan);
        EBookState actualState = sut_.getState();
        ILoan actualLoan = sut_.getLoan();
        
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
        sut_.borrow(expected);
        ILoan actual = sut_.getLoan();
        
        //assert
        assertEquals(expected, actual);
    }
    
    
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testReturnBookException () {
        //execute
        sut_.returnBook(false);
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    
    
    //Test returnBook with damaged book  
    @Test
    public void testReturnBookDamaged () {
        //arrange
        EBookState expected = EBookState.DAMAGED;
        boolean isdamaged = true;
        
        sut_.borrow(mock(ILoan.class));
        
        //execute
        sut_.returnBook(isdamaged);
        
        //assert
        EBookState actual = sut_.getState();
        assertEquals(expected, actual);
    }
    
    
    
    //Test returnBook with damaged book  
    @Test
    public void testReturnBookNotDamaged () {
        //arrange
        EBookState expected = EBookState.AVAILABLE;
        boolean isdamaged = false;
        
        sut_.borrow(mock(ILoan.class));
        
        //execute
        sut_.returnBook(isdamaged);
        
        //assert
        EBookState actual = sut_.getState();
        assertEquals(expected, actual);
    }
    
    
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testLoseException () {
        //execute
        sut_.lose();
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    
    
    //Test if correct state set if book is lost.
    @Test
    public void testLose() {
        //arrange
        EBookState expected = EBookState.LOST;
        sut_.borrow(mock(ILoan.class));
        
        //execute
        sut_.lose();
        
        //assert
        EBookState actual = sut_.getState();
        assertEquals(expected, actual);     
    }
    
    
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testRepairException () {
        //execute
        sut_.repair();
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    
    
    //Test if correct state set if book is lost.
    @Test
    public void testRepair() {
        //arrange
        EBookState expected = EBookState.AVAILABLE;
        
        sut_.borrow(mock(ILoan.class));    //Maybe these 2 lines should
        sut_.returnBook(true);             //be replaced with setState???
        
        //execute
        sut_.repair();
        
        //assert
        EBookState actual = sut_.getState();
        assertEquals(expected, actual);     
    }
    
    
    
    //Test if exception thrown if book is in incorrect state
    @Test(expected=RuntimeException.class)
    public void testDisposeException () {
        //arrange
        sut_.setState(EBookState.ON_LOAN);
        
        //execute
        sut_.dispose();
        
        //assert
        fail("Should have thrown a RuntimeException");
    }
    
    
    
    //Test if exception thrown if book is in incorrect state
    @Test
    public void testDispose () {
        //arrange
        EBookState expected = EBookState.DISPOSED;
        
        //execute
        sut_.dispose();
        
        //assert
        EBookState actual = sut_.getState();
        assertEquals(expected, actual); 
    }
}
