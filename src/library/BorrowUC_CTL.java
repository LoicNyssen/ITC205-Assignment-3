package library;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;

public class BorrowUC_CTL implements ICardReaderListener, 
                                     IScannerListener, 
                                     IBorrowUIListener {
    private IBorrowUI ui_;
    private EBorrowState state_;
    
    private ICardReader reader_;
    private IScanner scanner_;
    private IPrinter printer_;
    private IDisplay display_;
    
    private IBookDAO bookDAO_;
    private IMemberDAO memberDAO_;
    private ILoanDAO loanDAO_;
    
    private int scanCount_ = 0;
    
    private List<IBook> bookList_;
    private List<ILoan> loanList_;
    private IMember borrower_;
    
    private JPanel previous_;
    
    public BorrowUC_CTL(ICardReader reader, IScanner scanner, IPrinter printer, IDisplay display,
                        IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO ) {

        ui_    = new BorrowUC_UI(this);
        state_ = EBorrowState.CREATED;
        
        reader_  = reader;
        reader_.addListener(this);

        scanner_ = scanner;
        scanner_.addListener(this);
        
        printer_ = printer;
        display_ = display;
        
        bookDAO_   = bookDAO;
        loanDAO_   = loanDAO;
        memberDAO_ = memberDAO;
    }
    
    
    
    public BorrowUC_CTL(ICardReader reader, IScanner scanner, IPrinter printer, IDisplay display,
                        IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO, IBorrowUI ui ) {

        ui_    = ui;
        state_ = EBorrowState.CREATED;
        
        reader_  = reader;
        reader_.addListener(this);
        
        scanner_ = scanner;
        scanner_.addListener(this);
        
        printer_ = printer;
        display_ = display;
        
        bookDAO_   = bookDAO;
        loanDAO_   = loanDAO;
        memberDAO_ = memberDAO;
    }
    
    
    
    public void initialise() {
        setState(EBorrowState.INITIALIZED);
        previous_ = display_.getDisplay();
        display_.setDisplay((JPanel) ui_, "Borrow UI");
    }
    
    
    
    public void close() {
        display_.setDisplay(previous_, "Main Menu");
    }
    
    
    
    @Override
    public void cardSwiped(int memberID) {
        if (state_ != EBorrowState.INITIALIZED) {
            throw new RuntimeException("BorrowUC_CTL: cardSwiped : illegal operation in state: " + state_);
        }

        borrower_ = memberDAO_.getMemberByID(memberID);
        
        if (borrower_ == null) {
            ui_.displayErrorMessage("Cant find menber :" + memberID);
            return;
        }
        
        boolean dueLoans  = borrower_.hasOverDueLoans();
        boolean loanLimit = borrower_.hasReachedLoanLimit();
        boolean fineLimit = borrower_.hasReachedFineLimit();
        
        if (dueLoans || loanLimit || fineLimit) {
            setState(EBorrowState.BORROWING_RESTRICTED);
        } else {
            setState(EBorrowState.SCANNING_BOOKS);
        }
        
        ui_.displayMemberDetails(borrower_.getID(), 
                                 borrower_.getFirstName() + " " +
                                 borrower_.getLastName(),
                                 borrower_.getContactPhone());
        
        if (borrower_.hasFinesPayable()) {
            ui_.displayOutstandingFineMessage(borrower_.getFineAmount());
        }
        if (dueLoans) {
            ui_.displayOverDueMessage();
        }
        if (loanLimit) {
            ui_.displayAtLoanLimitMessage();
        }
        if (fineLimit) {
            ui_.displayOverFineLimitMessage(borrower_.getFineAmount());
        }
     
        ui_.displayExistingLoan(buildLoanListDisplay(borrower_.getLoans()));
    }
    
    
    
    @Override
    public void bookScanned(int barcode) {
        if (state_ != EBorrowState.SCANNING_BOOKS) {
            throw new RuntimeException("BorrowUC_CTL: bookScanned : illegal operation in state: " + state_);
        }
        
        IBook book = bookDAO_.getBookByID(barcode);
        
        if (book == null) {
            ui_.displayErrorMessage("Cant find book " + barcode);
            return;
        }
        
        if (book.getState() != EBookState.AVAILABLE) {
            ui_.displayErrorMessage("Book " + book.getID() + " is not available: "+ book.getState());
            return;
        }
            
        if (bookList_.contains(book)) {
            ui_.displayErrorMessage("Aready scanned book " + book.getID());
            return;
        }
        
        scanCount_++;
        bookList_.add(book);
        ILoan loan = loanDAO_.createLoan(borrower_, book);
        loanList_.add(loan);
        
        ui_.displayScannedBookDetails(book.getAuthor() + " " + book.getTitle());
        ui_.displayPendingLoan(buildLoanListDisplay(loanList_));
        
        if (scanCount_ >= IMember.LOAN_LIMIT) {
            setState(EBorrowState.CONFIRMING_LOANS);
        }            
    }
    
    
    
    private void setState(EBorrowState state) {
        state_ = state;
        ui_.setState(state_);
        
        switch (state) {
            case INITIALIZED:
                reader_.setEnabled(true);
                scanner_.setEnabled(false);
                break;
                
            case BORROWING_RESTRICTED:
                reader_.setEnabled(false);
                scanner_.setEnabled(false);
                break;
                
            case SCANNING_BOOKS:
                reader_.setEnabled(false);
                scanner_.setEnabled(true);
                bookList_ = new ArrayList<IBook>();
                loanList_ = new ArrayList<ILoan>();
                scanCount_ = borrower_.getLoans().size();
                
                ui_.displayScannedBookDetails(""); 
                
                ui_.displayPendingLoan("");  
                break;
                
            case CANCELLED:
                reader_.setEnabled(false);
                scanner_.setEnabled(false);
                close();
                break;
                
            case CREATED:
                this.reader_.setEnabled(false);
                this.scanner_.setEnabled(false);
                break;
                
            case CONFIRMING_LOANS:
                reader_.setEnabled(false);
                scanner_.setEnabled(false);
                ui_.displayConfirmingLoan(buildLoanListDisplay(loanList_));
                break;
                
            case COMPLETED:
                reader_.setEnabled(false);
                scanner_.setEnabled(false);
                
                for (ILoan loan : loanList_) {
                    loanDAO_.commitLoan(loan);
                }
                
                printer_.print(buildLoanListDisplay(loanList_));
                
                close();
                break;
            default:
                throw new RuntimeException("BorrowUC_CTL: setState : unknown state: " + state_);
        }
    }
    
    
    
    @Override
    public void cancelled() {
        setState(EBorrowState.CANCELLED);
    }
    
    
    
    @Override
    public void scansCompleted() {
        setState(EBorrowState.CONFIRMING_LOANS);
    }

    
    
    @Override
    public void loansConfirmed() {
        setState(EBorrowState.COMPLETED);
    }

    
    
    @Override
    public void loansRejected() {
        setState(EBorrowState.SCANNING_BOOKS);
    }

    
    
    public String buildLoanListDisplay(List<ILoan> loans) {
        StringBuilder bld = new StringBuilder();
        for (ILoan ln : loans) {
            if (bld.length() > 0) bld.append("\n\n");
            bld.append(ln.toString());
        }
        return bld.toString();      
    }
    
    
    public List<ILoan> getLoanList () {
        return loanList_;
    }
    
    
    
    public List<IBook> getBookList () {
        return bookList_;
    }
    
    
    
    public EBorrowState getState() {
        return state_;
    }
    
    
    
    public int getScanCount () {
        return scanCount_;
    }
}
