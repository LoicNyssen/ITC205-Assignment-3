package libary.LowLevelIntegration;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import library.entities.Book;
import library.entities.Loan;
import library.entities.Member;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public class BookTest {
    
    private String author_;
    private String title_;
    private String callNumber_;
    private int id_;
    
    private IBook book_;
    private ILoan loan_;
    private IMember member_;
    
    private Calendar calendar_ = Calendar.getInstance();
    
    private Date borrowDate_ = new Date();
    private Date dueDate_;
    
    
    
    @Before
    public void setUp() throws Exception {
        author_     = "Author";
        title_      = "Title";
        callNumber_ = "1";
        id_         = 1;
        
        book_   = new Book(author_, title_, callNumber_, id_);
        member_ = new Member("Loic", "Nyssen", "0404123123", "loic@nyssen.com.au", 1);
        
        calendar_.setTime(borrowDate_);
        calendar_.add(Calendar.DATE, ILoan.LOAN_PERIOD);
        dueDate_ = calendar_.getTime();
        
        loan_ = new Loan(book_, member_, borrowDate_, dueDate_);
    }
    
    @Test
    public void testBorrowAvailable() {
        //arrange
        EBookState expected = EBookState.ON_LOAN;
        
        //execute
        book_.borrow(loan_);
        EBookState actual = book_.getState();
        
        //assert
        assertEquals(expected, actual);
    }
    
    @Test(expected=RuntimeException.class)
    public void testBorrowNotAvailable() {

        //arrange
        book_.borrow(loan_); 
        
        //execute
        book_.borrow(loan_);        
        
        fail("Should have thrown an exception");

    }
}
