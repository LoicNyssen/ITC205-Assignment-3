package library.entities;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;

public class Book implements IBook {

    private String author_;
    private String title_;
    private String callNumber_;
    private int id_;

    private ILoan loan_;
    private EBookState state_;


    public Book(String author, String title, String callNumber, int bookID)
    {
        if (!sane(author, title, callNumber, bookID)) {
            throw new IllegalArgumentException("Book : constructor : Invalid arguments");
        }
        author_     = author;
        title_      = title;
        callNumber_ = callNumber;
        id_         = bookID;
        state_      = EBookState.AVAILABLE;
        loan_       = null;
    }

    
    
    private boolean sane(String author, String title, String callNumber, int bookID) {
        return (author     != null && (!author.isEmpty()) &&
                title      != null && (!title.isEmpty()) &&
                callNumber != null && (!callNumber.isEmpty()) &&
                bookID > 0);
    }

    
    
    @Override
    public void borrow(ILoan loan) {
        if (state_ != EBookState.AVAILABLE) {
            throw new RuntimeException("Book : borrow : State must be available");
        }
        loan_  = loan;
        state_ = EBookState.ON_LOAN;
    }

    
    
    @Override
    public ILoan getLoan() {
        return loan_;
    }

    
    
    @Override
    public void returnBook(boolean damaged) {
        if (state_ != EBookState.ON_LOAN) {
            throw new RuntimeException("Book : returnBook : State must be on_loan");
        }

        loan_ = null;
        if (damaged) {
            state_ = EBookState.DAMAGED;
        }

        else {
            state_ = EBookState.AVAILABLE;
        }
    }

    
    
    @Override
    public void lose() {
        if (state_ != EBookState.ON_LOAN) {
            throw new RuntimeException("Book : lose : State must be on_loan");
        }

        state_ = EBookState.LOST;
    }

    
    
    @Override
    public void repair() {
        if (state_ != EBookState.DAMAGED) {
            throw new RuntimeException("Book : repair : State must be damaged");
        }

        state_ = EBookState.AVAILABLE;
    }

    
    
    @Override
    public void dispose() {
        if ((state_ != EBookState.AVAILABLE) && (state_ != EBookState.DAMAGED) && (state_ != EBookState.LOST)) {
            throw new RuntimeException("Book : repair : State must be available or damaged or lost");
        }
        state_ = EBookState.DISPOSED;
    }

    
    
    @Override
    public EBookState getState() {
        return state_;
    }
    
    
    
    public void setState(EBookState state) {
        state_ = state;
    }

    
    
    @Override
    public String getAuthor() {
        return author_;
    }

    
    
    @Override
    public String getTitle() {
        return title_;
    }

    
    
    @Override
    public String getCallNumber() {
        return callNumber_;
    }

    
    
    @Override
    public int getID() {
        return id_;
    }
}
